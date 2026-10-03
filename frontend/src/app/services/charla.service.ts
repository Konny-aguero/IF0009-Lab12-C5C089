import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
export interface Asistente {
  id?: number;
  nombre: string;
  correo: string;
  edad: number;
}
export interface Charla {
  id?: number;
  titulo: string;
  expositor: string;
  nivel: string;
  emailContacto: string;
  fechaInicio: string;
  fechaFin: string;
  etiquetas: string[];
  asistentes?: Asistente[];
}
@Injectable({providedIn: 'root'})
export class CharlaService {
  private http = inject(HttpClient);
  private apiUrl = 'http://localhost:8080/api/charlas';
  inscribirAsistente(id: number, asistente: Asistente) {
    return this.http.post<Asistente>(`${this.apiUrl}/${id}/asistentes`, asistente);
  }
  getCharlas() { return this.http.get<Charla[]>(this.apiUrl); }
  registrarCharla(charla: Charla) { return this.http.post<Charla>(this.apiUrl, charla); }
}
