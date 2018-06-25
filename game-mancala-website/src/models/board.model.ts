export interface IBoardCreate {
  boardSize?: number,
  stonesPerPit?: number
}

export interface IBoard {
  id: string,
  currentPlayer: string,
  boardSizePerPlayer: number,
  isOpen: boolean,
  winner?: string,
  board: number[]
}
