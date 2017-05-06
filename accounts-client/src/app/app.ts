//our root app component
import {Component, NgModule} from '@angular/core'
import {BrowserModule} from '@angular/platform-browser'

import {Tabs} from '../tabs/tabs';
import {Tab} from '../tabs/tab';

@Component({
  selector: 'bank-app',
  template: `
    <tabs>
      <tab tabTitle="find account">
        Find Account
      </tab>
      <tab tabTitle="account statement">
        Tab 2 Content
      </tab>
      <tab tabTitle="transfer money">
        Tab 3 Content
      </tab>
    </tabs>
  `
})

export class App {
  name
  constructor() {
    this.name = 'Angular2'
  }
}

@NgModule({
  imports: [ BrowserModule ],
  declarations: [ App, Tabs, Tab ],
  bootstrap: [ App]
})
export class AppModule {}
