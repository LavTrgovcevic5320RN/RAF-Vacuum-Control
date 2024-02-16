import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { UserService } from '../service/user.service';
import { User } from '../model/user';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  standalone: true,
  imports: [FormsModule, CommonModule],
  selector: 'app-add-user',
  templateUrl: './add-user.component.html',
  styleUrls: ['./add-user.component.css']
})
export class AddUserComponent {
  user: User = {
    firstName: "",
    lastName: "",
    permissions: [],
    email: "",
    password: ""
  };

  constructor(private userService: UserService, private router: Router) { }

  onSubmit(): void {
    this.userService.createUser(this.user).subscribe((createdUser: any) => {
      console.log('User created successfully:', createdUser);

      alert("Success!");
      this.router.navigate(['/all']);
    }, (error: any) => {
      alert("Something went wrong");
      console.error('Error creating user:', error);
    });
  }
}
