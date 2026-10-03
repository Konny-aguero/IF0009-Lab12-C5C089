import { Component } from '@angular/core';
import { CharlaRegistroComponent } from './components/charla-registro/charla-registro.component';
@Component({selector: 'app-root', standalone: true, imports: [CharlaRegistroComponent], template: '<header><h1>TechConf</h1><p>Registro de charlas y agenda</p></header><main><app-charla-registro /></main>'})
export class AppComponent {}
