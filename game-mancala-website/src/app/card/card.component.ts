import {Component, EventEmitter, Input, OnInit} from '@angular/core'
import {HttpClient} from '@angular/common/http'
import {environment} from 'environments/environment'
import {IBoard} from 'models/board.model'
import {ICardBroadcast} from 'models/card.model'
import {distinctUntilChanged} from 'rxjs/operators'
import {BoardComponentEmitter} from '../board/board.component'

export class CardComponentEmitter {
  public eventBroadcast: EventEmitter<ICardBroadcast> = new EventEmitter()
}

@Component({
  selector: 'app-card',
  templateUrl: './card.component.html',
  styleUrls: ['./card.component.css']
})
export class CardComponent implements OnInit {

  @Input() boardId: string
  @Input() id: number
  @Input() stone: number

  constructor(private cardComponentEmitter: CardComponentEmitter,
              private boardComponentEmitter: BoardComponentEmitter,
              private httpClient: HttpClient) {
  }

  public targetStone() {
    this.httpClient.put(`${environment.baseUrl}/board/${this.boardId}/target/${this.id}`, null).subscribe(
      (board: IBoard) => {
        this.broadcastBoard(board)
        this.emitBoardComponent(board)
      }
    )
  }

  private broadcastBoard(board: IBoard) {
    board.board.forEach((stone, id) => {
      const broadcast: ICardBroadcast = {
        id: id,
        stones: stone
      }
      this.cardComponentEmitter.eventBroadcast.emit(broadcast)
    })
  }

  private emitBoardComponent(board: IBoard) {
    this.boardComponentEmitter.eventBoard.emit(board)
  }

  ngOnInit() {
    this.cardComponentEmitter.eventBroadcast
      .pipe(distinctUntilChanged())
      .subscribe(
        (broadcast: ICardBroadcast) => {
          if (broadcast.id === this.id) {
            this.stone = broadcast.stones
          }
        }
      )
  }
}
