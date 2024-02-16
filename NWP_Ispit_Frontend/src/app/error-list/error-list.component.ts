import { Component } from '@angular/core';
import { Vacuum } from '../vacuum-list/vacuum-list.component';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ErrorService } from '../service/error.service';
import { AuthService } from '../service/auth.service';
import { HttpErrorResponse } from '@angular/common/http';

export interface OperationError {
  id: number;
  vacuum: Vacuum;
  operation: string;
  errorMessage: string;
  scheduledTime: string;
}

@Component({
  selector: 'app-error-list',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './error-list.component.html',
  styleUrl: './error-list.component.css'
})
export class ErrorListComponent {
  errors: OperationError[] = [];

  constructor(private errorService: ErrorService, private authService: AuthService){
  }

  ngOnInit(): void {
    this.loadErrors();
  }

  loadErrors(): void {
    this.errorService.getErrors().subscribe(
      (errs: OperationError[]) => {
        this.errors = errs;
      },
      (error: HttpErrorResponse) => {
        console.log("Error loading vacuums: ", error);
      }
    )
  }
}
