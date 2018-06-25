import {Component, EventEmitter, OnInit} from '@angular/core'
import {ActivatedRoute} from '@angular/router'
import {ParamMap} from '@angular/router/src/shared'
import {find, map} from 'rxjs/operators'
import {HttpClient} from '@angular/common/http'
import {environment} from 'environments/environment'
import {IBoard} from 'models/board.model'

export class BoardComponentEmitter {
  public eventBoard: EventEmitter<IBoard> = new EventEmitter()
}

export interface BoardStone {
  index: number,
  stones: number
}

@Component({
  selector: 'app-board',
  templateUrl: './board.component.html',
  styleUrls: ['./board.component.css']
})
export class BoardComponent implements OnInit {

  private keyId = 'id'
  private board: IBoard

  public collectorPlayer1: BoardStone
  public boardPlayer1: BoardStone[]

  public collectorPlayer2: BoardStone
  public boardPlayer2: BoardStone[]

  constructor(private routes: ActivatedRoute,
              private httpClient: HttpClient,
              private boardComponentEmitter: BoardComponentEmitter) {
    routes.paramMap.pipe(
      find((paramMap: ParamMap) => paramMap.has(this.keyId)),
      map((paramMap: ParamMap) => paramMap.get(this.keyId))
    ).subscribe((id: string) => this.getBoard(id))
  }

  private getBoard(id: string) {
    this.httpClient.get(`${environment.baseUrl}/board/${id}`).subscribe(
      (board: IBoard) => this.mappingBoard(board)
    )
  }

  private mappingBoard(board: IBoard) {
    const indexedBoard: BoardStone[] = board.board.map((stone, index) => {
      const subItem: BoardStone = {index: index, stones: stone}
      return subItem
    })
    this.collectorPlayer1 = indexedBoard.shift()
    this.collectorPlayer2 = indexedBoard.splice(board.boardSizePerPlayer - 1, 1)[0]
    this.boardPlayer1 = indexedBoard.splice(0, indexedBoard.length / 2)
    this.boardPlayer2 = indexedBoard.reverse()
    this.board = board
  }

  ngOnInit() {
    this.boardComponentEmitter.eventBoard.subscribe(
      (board: IBoard) => this.board = board
    )
  }

}
