import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { VacuumServiceService } from '../service/vacuum.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-add-vacuum',
  standalone: true,
  imports: [FormsModule, CommonModule],
  templateUrl: './add-vacuum.component.html',
  styleUrl: './add-vacuum.component.css'
})
export class AddVacuumComponent {
  name = '';

  constructor(private vacuumService: VacuumServiceService, private router: Router) {}

  onSubmit(): void {
    this.vacuumService.createVacuum(this.name).subscribe((createdVacuum: any) => {
      console.log('Vacuum created successfully:', createdVacuum);

      alert("Success!");
      this.router.navigate(['/all']);
    });
  }
}
