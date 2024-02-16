import { Component, OnInit } from '@angular/core';
import { VacuumServiceService } from '../service/vacuum.service';
import { CommonModule } from '@angular/common';
import { HttpErrorResponse } from '@angular/common/http';
import { AuthService } from '../service/auth.service';
import { FormsModule } from '@angular/forms';

export interface Vacuum {
  id: number;
  name: string;
  status: string;
  refCnt: number;
  addedBy: any;
  createdAt: string;
}

@Component({
  selector: 'app-vacuum-list',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './vacuum-list.component.html',
  styleUrl: './vacuum-list.component.css'
})
export class VacuumListComponent implements OnInit{
  vacuums: Vacuum[] = [];
  hasPermission: boolean = true;
  name = ''
  status = ''
  dateFrom = ''
  dateTo = ''
  startVacuumIdx = ''
  stopVacuumIdx = ''
  discVacuumIdx = ''
  operation = ''
  schedOpIdx = ''
  schedTime = null
  hasSearchPerm = false;

  constructor(private vacuumService: VacuumServiceService, private authService: AuthService){

  }

  ngOnInit(): void {
    this.loadVacuums();
    this.hasPermission = this.authService.hasPermission('can_update_users');
    this.hasSearchPerm = this.authService.hasPermission('can_search_vacuums');
  }

  onSubmit(event: { preventDefault: () => void; }) {
    event.preventDefault();

    this.loadVacuums();
  }

  loadVacuums(): void {
    this.vacuumService.getVacuums(this.name, this.status, this.dateFrom, this.dateTo).subscribe(
      (vacuums: Vacuum[]) => {
        this.vacuums = vacuums;
      },
      (error: HttpErrorResponse) => {
        console.log("Error loading vacuums: ", error);
      }
    )
  }

  startVacuum() {
    this.vacuumService.startVacuum(this.startVacuumIdx).subscribe(res => {
      alert(res.msg)
    }, error => {
      alert(error.message);
    })
  }

  stopVacuum() {
    this.vacuumService.stopVacuum(this.stopVacuumIdx).subscribe(res => {
      alert(res.msg)
    }, error => {
      alert(error.message);
    })
  }

  discVacuum() {
    this.vacuumService.discVacuum(this.discVacuumIdx).subscribe(res => {
      alert(res.msg)
    }, error => {
      alert(error.message);
    })
  }

  scheduleOperation() {
    // @ts-ignore
    this.vacuumService.scheduleOperation(this.schedOpIdx, this.operation, this.schedTime).subscribe(res => {
      alert(res.msg)
    }, error => {
      alert(error.message);
    })
  }

  hide(permission: string):boolean {
    return !this.authService.hasPermission(permission);
  }
}
