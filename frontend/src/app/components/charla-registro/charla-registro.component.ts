import { Component, inject, OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { AsistenteRegistroComponent } from '../asistente-registro/asistente-registro.component';
import { Asistente, Charla, CharlaService } from '../../services/charla.service';
import { validarRangoFechas } from '../../validators/validadores';
@Component({selector: 'app-charla-registro', standalone: true, imports: [ReactiveFormsModule, DatePipe, AsistenteRegistroComponent], templateUrl: './charla-registro.component.html'})
export class CharlaRegistroComponent implements OnInit {
  private fb = inject(FormBuilder).nonNullable;
  private servicio = inject(CharlaService);
  charlas: Charla[] = [];
  charlaSeleccionada?: number;
  actualizarAsistentes(charla: Charla, asistente: Asistente) {
    charla.asistentes = [...(charla.asistentes ?? []), asistente];
    this.charlaSeleccionada = undefined;
    this.mensajeExito = '¡Asistente inscrito exitosamente!';
  }
  mensajeExito = '';
  error = '';
  guardando = false;
  registroForm = this.fb.group({
    titulo: ['', [Validators.required, Validators.minLength(5)]],
    expositor: ['', Validators.required],
    nivel: ['Principiante', Validators.required],
    emailContacto: ['', [Validators.required, Validators.email]],
    fechaInicio: ['', Validators.required],
    fechaFin: ['', Validators.required],
    etiquetas: this.fb.array([this.fb.control('', Validators.required)])
  }, {validators: validarRangoFechas});
  get etiquetasArray() { return this.registroForm.controls.etiquetas; }
  ngOnInit() { this.cargarCharlas(); }
  cargarCharlas() {
    this.servicio.getCharlas().subscribe({next: datos => this.charlas = datos, error: () => this.error = 'No se pudieron cargar las charlas.'});
  }
  agregarEtiqueta() { this.etiquetasArray.push(this.fb.control('', Validators.required)); }
  removerEtiqueta(index: number) { if (this.etiquetasArray.length > 1) this.etiquetasArray.removeAt(index); }
  onSubmit() {
    if (this.registroForm.invalid || this.guardando) { this.registroForm.markAllAsTouched(); return; }
    this.guardando = true;
    this.error = '';
    this.mensajeExito = '';
    this.servicio.registrarCharla(this.registroForm.getRawValue()).subscribe({
      next: charla => {
        this.charlas = [...this.charlas, charla];
        this.etiquetasArray.clear();
        this.agregarEtiqueta();
        this.registroForm.reset();
        this.mensajeExito = '¡Charla registrada exitosamente!';
        this.guardando = false;
      },
      error: () => { this.error = 'No se pudo guardar la charla. Revise los datos y la conexión.'; this.guardando = false; }
    });
  }
}
