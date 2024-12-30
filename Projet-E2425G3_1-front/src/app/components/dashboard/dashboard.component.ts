import { AfterViewInit, Component, OnInit } from '@angular/core';
import { Chart } from 'chart.js';


@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.scss'
})
/*export class DashboardComponent {
  
  
}
*/

export class DashboardComponent implements AfterViewInit {
  ngAfterViewInit() {
    // Weekly Average Chart
    new Chart('weeklyAverageChart', {
      type: 'bar',
      data: {
        labels: ['10', '11', '12'],
        datasets: [
          {
            label: 'Spending Time',
            data: [3, 11, 3],
            backgroundColor: ['#007bff'],
          },
        ],
      },
    });

    // Gauge Chart
    new Chart('gaugeChart', {
      type: 'doughnut',
      data: {
        labels: ['Normal', 'Medium', 'High'],
        datasets: [
          {
            data: [50, 30, 20],
            backgroundColor: ['blue', 'orange', 'red'],
          },
        ],
      },
      options: {
        cutout: '80%',
      },
    });

    // Doughnut Chart
    new Chart('doughnutChart', {
      type: 'doughnut',
      data: {
        labels: ['Option A', 'Option B', 'Option C'],
        datasets: [
          {
            data: [60, 20, 20],
            backgroundColor: ['blue', 'pink', 'orange'],
          },
        ],
      },
    });
  }
}
