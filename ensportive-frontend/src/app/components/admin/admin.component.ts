import { Component, OnInit } from '@angular/core';
import { AsyncPipe, DecimalPipe } from '@angular/common';
import { FormControl, ReactiveFormsModule } from '@angular/forms';
import { IconsModule } from '../../icons/icons.module';
import { Observable } from 'rxjs';
import { map, startWith, switchMap } from 'rxjs/operators';
import { NgbHighlight } from '@ng-bootstrap/ng-bootstrap';
import { Admin } from '../../interfaces/admin.interface';
import { AdminService } from '../../services/admin/admin.service';
import { NewAdminModalComponent } from '../new-admin-modal/new-admin-modal.component';
import { HttpClientModule } from '@angular/common/http';
import { ConfirmModalComponent } from '../confirm-modal/confirm-modal.component';

@Component({
  selector: 'app-admin',
  standalone: true,
  imports: [
    DecimalPipe,
    AsyncPipe,
    ReactiveFormsModule,
    NgbHighlight,
    IconsModule,
    NewAdminModalComponent,
    ConfirmModalComponent,
    HttpClientModule,
  ],
  templateUrl: './admin.component.html',
  styleUrl: './admin.component.scss',
  providers: [DecimalPipe, AdminService],
})
export class AdminComponent implements OnInit {
  admins$!: Observable<Admin[]>;
  filteredAdmins$!: Observable<Admin[]>;
  filter = new FormControl('', { nonNullable: true });

  constructor(private adminService: AdminService) {}

  ngOnInit() {
    this.getAllAdmins();
  }

  getAllAdmins() {
    this.admins$ = this.adminService.getAdmins();

    this.filteredAdmins$ = this.filter.valueChanges.pipe(
      startWith(''),
      switchMap(text => this.search(text))
    );
  }

  search(text: string): Observable<Admin[]> {
    const term = text.toLowerCase();
    return this.admins$.pipe(
      map(admins =>
        admins.filter(admin => {
          return (
            admin.username.toLowerCase().includes(term) ||
            admin.password.toLowerCase().includes(term)
          );
        })
      )
    );
  }

  // handleDelete(id: string | null) {
  //   if (id === null) {
  //     return;
  //   }
  //   this.adminService.deleteAdmin(id).subscribe({
  //     next: () => {
  //       this.getAllAdmins();
  //     },
  //     error: error => console.error('Error deleting admin:', error),
  //   });
  // }

  handleSave(admin: Admin) {
    if (admin.id) {
      this.adminService.updateAdmin(admin).subscribe({
        next: () => this.getAllAdmins(),
        error: error => console.error('Erro ao atualizar administrador', error),
      });
    } else {
      this.adminService.saveAdmin(admin).subscribe({
        next: () => this.getAllAdmins(),
        error: error => console.error('Erro ao criar administrador', error),
      });
    }
  }
}
