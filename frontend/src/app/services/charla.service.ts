import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
export interface Charla {
  id?: number;
  titulo: string;
  expositor: string;
  nivel: string;
  emailContacto: string;
  fechaInicio: string;
  fechaFin: string;
  etiquetas: string[];
}
@Injectable({providedIn: 'root'})
export class CharlaService {
  private http = inject(HttpClient);
  private apiUrl = 'http://localhost:8080/api/charlas';
  getCharlas() { return this.http.get<Charla[]>(this.apiUrl); }
  registrarCharla(charla: Charla) { return this.http.post<Charla>(this.apiUrl, charla); }
}
