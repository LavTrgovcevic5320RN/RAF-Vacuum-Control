import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import {CommonModule, NgFor, NgForOf, NgIf} from "@angular/common";
import {NavbarComponent} from "./navbar/navbar.component";

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, NgIf, NgFor, NgForOf, CommonModule, NavbarComponent],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
  title = 'NWP_Ispit_Frontend';
}
