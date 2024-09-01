import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { BootstrapIconsModule } from 'ng-bootstrap-icons';
import {
  Bell,
  BoxArrowRight,
  PencilFill,
  PlusSquare,
  FunnelFill,
  Search,
  At,
  Eye,
  EyeSlash,
  Lock,
  PersonCircle,
  CheckCircle,
  XCircle,
  Trash,
  PersonFill,
  TelephoneFill,
  EyeFill,
  ArrowLeft,
} from 'ng-bootstrap-icons/icons';

const icons = {
  Bell,
  BoxArrowRight,
  PencilFill,
  PlusSquare,
  FunnelFill,
  Search,
  At,
  Eye,
  EyeSlash,
  Lock,
  PersonCircle,
  CheckCircle,
  XCircle,
  Trash,
  PersonFill,
  TelephoneFill,
  EyeFill,
  ArrowLeft,
};

@NgModule({
  declarations: [],
  imports: [CommonModule, BootstrapIconsModule.pick(icons)],
  exports: [BootstrapIconsModule],
})
export class IconsModule {}
