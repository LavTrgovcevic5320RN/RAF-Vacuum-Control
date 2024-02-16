import { Routes } from '@angular/router';
import { LoginComponent } from './login/login.component';
import { AddUserComponent } from './add-user/add-user.component';
import { EditUserComponent } from './edit-user/edit-user.component';
import { UserListComponent } from './user-list/user-list.component';
import { canReadUsersGuard } from './guards/can-read-users.guard'
import { canCreateUsersGuard } from './guards/can-create-users.guard';
import { canUpdateUsersGuard } from './guards/can-update-users.guard';
import { VacuumListComponent } from './vacuum-list/vacuum-list.component';
import { AddVacuumComponent } from './add-vacuum/add-vacuum.component';
import { ErrorListComponent } from './error-list/error-list.component';

export const routes: Routes = [
  {path: 'login', component: LoginComponent},
  {path: 'add', component: AddUserComponent, canActivate: [canCreateUsersGuard]},
  {path: 'edit/:id', component: EditUserComponent, canActivate: [canUpdateUsersGuard]},
  {path: 'all', component: UserListComponent, canActivate: [canReadUsersGuard]},
  {path: 'vacuums', component: VacuumListComponent},
  {path: 'vacuums-add', component: AddVacuumComponent},
  {path: 'errors', component: ErrorListComponent}
];
