import {Component} from '@angular/core'
import {Router} from '@angular/router'
import {HttpClient} from '@angular/common/http'
import {IBoard, IBoardCreate} from 'models/board.model'
import {environment} from 'environments/environment'
import {FormControl, Validators} from '@angular/forms'

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent {

  boardCreate: IBoardCreate = {}

  boardSizeControl = new FormControl('', [Validators.required])
  stonesPerPitControl = new FormControl('', [Validators.required])

  constructor(private router: Router,
              private httpClient: HttpClient) {
  }

  createGame() {
    this.httpClient.post(`${environment.baseUrl}/board`, this.boardCreate).subscribe(
      (board: IBoard) => this.router.navigate([`board/${board.id}`])
    )
  }
}
