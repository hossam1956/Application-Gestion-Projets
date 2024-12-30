import { Component } from '@angular/core';
import { RouterOutlet, RouterLink} from '@angular/router';
@Component({
  selector: 'app-default',
  standalone: true,
  imports: [RouterOutlet, RouterLink],
  templateUrl: './default.component.html',
  styleUrl: './default.component.scss'
})
export class DefaultComponent {

}
