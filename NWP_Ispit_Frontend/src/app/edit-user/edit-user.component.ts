import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { UserService } from '../service/user.service';
import { User } from '../model/user';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  standalone: true,
  imports: [FormsModule, CommonModule],
  selector: 'app-edit-user',
  templateUrl: './edit-user.component.html',
  styleUrls: ['./edit-user.component.css']
})
export class EditUserComponent implements OnInit {
  userId!: number;
  user!: User;

  constructor(private route: ActivatedRoute, private userService: UserService) { }

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      this.userId = +params['id'];

      this.userService.getUserById(this.userId).subscribe(user => {
        this.user = user;
      });
    });
  }

  onSubmit(): void {
    this.userService.updateUser(this.userId, this.user).subscribe(updatedUser => {
      alert('User updated successfully:' + updatedUser.firstName);

      window.location.href = "/all"
    }, error => {
      console.error('Error updating user:', error);
    });
  }
}
