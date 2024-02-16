import { Component, OnInit } from '@angular/core';
import { UserService } from '../service/user.service';
import { User } from '../model/user';
import { HttpErrorResponse } from '@angular/common/http';
import { CommonModule } from '@angular/common';
import { AuthService } from '../service/auth.service';

@Component({
  standalone: true,
  selector: 'app-user-list',
  templateUrl: './user-list.component.html',
  styleUrls: ['./user-list.component.css'],
  imports: [CommonModule]
})
export class UserListComponent implements OnInit {
  users: User[] = [];
  hasPermission: boolean = true;

  constructor(private userService: UserService, private authService: AuthService) { }

  ngOnInit(): void {
    this.loadUsers();
    this.hasPermission = this.authService.hasPermission('can_update_users');
  }

  loadUsers(): void {
    this.userService.getUsers().subscribe(
      (users: User[]) => {
        this.users = users;
        console.log(users)
      },
      (error: HttpErrorResponse) => {
        console.error('Error loading users:', error);
      }
    );
  }

  deleteUser(userId: number | undefined) {
    if (userId != null) {
      this.userService.deleteUser(userId).subscribe(() => {

        this.loadUsers();
      });
    }
  }

  // @ts-ignore
  hide(permission):boolean {
    return !this.authService.hasPermission(permission);
  }
}
