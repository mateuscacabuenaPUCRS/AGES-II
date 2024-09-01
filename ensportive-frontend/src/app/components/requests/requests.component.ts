import { AsyncPipe, CommonModule, DecimalPipe } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormControl, ReactiveFormsModule } from '@angular/forms';
import { NgbHighlight } from '@ng-bootstrap/ng-bootstrap';
import { Observable } from 'rxjs';
import { map, startWith, switchMap } from 'rxjs/operators';
import { IconsModule } from '../../icons/icons.module';
import { Request } from '../../interfaces/requests/request.interface';
import { AbsenceRequest } from '../../interfaces/requests/absence-request.interface';
import {
  AuthContextService,
  AuthUser,
} from '../../services/auth/auth-context.service';
import { RequestService } from '../../services/request/request.service';
import { AbsenceRequestModalComponent } from '../absence-request-modal/absence-request-modal.component';
import { AdminRequestModalComponent } from '../admin-request-modal/admin-request-modal.component';
import { ConfirmModalComponent } from '../confirm-modal/confirm-modal.component';
import {
  RequestStatus,
  requestStatusToString,
} from '../../enums/request-status';
import { requestTypeToString } from '../../enums/request-type';

@Component({
  selector: 'app-requests',
  standalone: true,
  imports: [
    DecimalPipe,
    AsyncPipe,
    ReactiveFormsModule,
    NgbHighlight,
    IconsModule,
    ConfirmModalComponent,
    AbsenceRequestModalComponent,
    AdminRequestModalComponent,
    [CommonModule],
  ],
  templateUrl: './requests.component.html',
  styleUrl: './requests.component.scss',
  providers: [DecimalPipe],
})
export class RequestsComponent implements OnInit {
  requests$!: Observable<Request[]>;
  filteredRequests$!: Observable<Request[]>;
  filter = new FormControl('', { nonNullable: true });
  user: AuthUser | null = null;
  isAdmin: boolean = false;

  constructor(
    private requestService: RequestService,
    private authContextService: AuthContextService
  ) {}

  ngOnInit() {
    this.authContextService.getUser().subscribe((user: AuthUser) => {
      this.user = user;
      this.isAdmin = user.roles.includes('ADMIN');
      this.getRequests();
    });
  }

  getRequests() {
    this.requests$ = this.requestService.getRequests();

    this.requests$.subscribe(requests => {
      console.log('Requests:', requests);
    });
    this.filteredRequests$ = this.filter.valueChanges.pipe(
      startWith(''),
      switchMap(text => this.search(text))
    );
  }

  search(text: string): Observable<Request[]> {
    const term = text.toLowerCase();

    return this.requests$.pipe(
      map(requests =>
        requests.filter(request => {
          return (
            request.userName?.toLowerCase().includes(term) ||
            request.requestStatus?.toString().toLowerCase().includes(term)
          );
        })
      )
    );
  }

  handleSaveAbsence(request: AbsenceRequest) {
    this.requestService.createAbsenceRequest(request).subscribe(() => {
      this.getRequests();
    });
  }

  handleRequestApproval(event: { requestId: string | null; action: string }) {
    const { requestId, action } = event;
    if (requestId === null) {
      return;
    }
    if (action === 'deny') {
      this.requestService.denyRequest(requestId).subscribe(
        () => {
          this.getRequests();
        },
        error => {
          console.error('Error denying request:', error);
        }
      );
    } else if (action === 'approve') {
      this.requestService.approveRequest(requestId).subscribe(
        () => {
          this.getRequests();
        },
        error => {
          console.error('Error approving request:', error);
        }
      );
    }
  }

  handleDelete(id: string | null) {
    if (id === null) {
      return;
    }
    this.requestService.deleteRequest(id);
  }

  formatDate(date?: string) {
    if (!date) {
      return '';
    }
    return new Date(date).toLocaleString().replace(',', ' ');
  }

  getRequestStatusToString(status: RequestStatus) {
    return requestStatusToString(status);
  }

  getRequestTypeToString(type: string) {
    return requestTypeToString(type);
  }
}
