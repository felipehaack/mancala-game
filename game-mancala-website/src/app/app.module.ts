import {BrowserModule} from '@angular/platform-browser'
import {NgModule} from '@angular/core'

import {AppComponent} from './app.component'
import {CardComponent, CardComponentEmitter} from './card/card.component'
import {BoardComponent, BoardComponentEmitter} from './board/board.component'
import {RouterModule, Routes} from '@angular/router'
import {FormsModule, ReactiveFormsModule} from '@angular/forms'
import {BrowserAnimationsModule} from '@angular/platform-browser/animations'
import {HomeComponent} from './home/home.component'
import {HttpClientModule} from '@angular/common/http'
import {MatButtonModule, MatSelectModule} from '@angular/material'

const appRoutes: Routes = [
  {
    path: 'board/:id',
    component: BoardComponent
  },
  {
    path: '**',
    component: HomeComponent
  }
]

export const rooting = RouterModule.forRoot(appRoutes)

@NgModule({
  declarations: [
    AppComponent,
    CardComponent,
    BoardComponent,
    HomeComponent
  ],
  imports: [
    ReactiveFormsModule,
    BrowserModule,
    BrowserAnimationsModule,
    FormsModule,
    BrowserModule,
    HttpClientModule,
    MatSelectModule,
    MatButtonModule,
    rooting
  ],
  providers: [
    CardComponentEmitter,
    BoardComponentEmitter
  ],
  bootstrap: [
    AppComponent
  ]
})
export class AppModule {
}
