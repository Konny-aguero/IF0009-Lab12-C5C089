import { Component, EventEmitter, inject, Input, Output } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Asistente, CharlaService } from '../../services/charla.service';
import { mayorDe18 } from '../../validators/validadores';
@Component({selector: 'app-asistente-registro', standalone: true, imports: [ReactiveFormsModule], templateUrl: './asistente-registro.component.html'})
export class AsistenteRegistroComponent {
  @Input({required: true}) charlaId!: number;
  @Output() inscrito = new EventEmitter<Asistente>();
  private fb = inject(FormBuilder);
  private servicio = inject(CharlaService);
  guardando = false;
  error = '';
  inscripcionForm = this.fb.group({
    asistente: this.fb.group({
      nombre: this.fb.nonNullable.control('', [Validators.required, Validators.minLength(3)]),
      correo: this.fb.nonNullable.control('', [Validators.required, Validators.email]),
      edad: this.fb.control<number | null>(null, [Validators.required, mayorDe18])
    })
  });
  get datos() { return this.inscripcionForm.controls.asistente.controls; }
  onSubmit() {
    if (this.inscripcionForm.invalid || this.guardando) { this.inscripcionForm.markAllAsTouched(); return; }
    const datos = this.inscripcionForm.getRawValue().asistente;
    if (datos.edad === null) return;
    this.guardando = true;
    this.error = '';
    this.servicio.inscribirAsistente(this.charlaId, {...datos, edad: datos.edad}).subscribe({
      next: asistente => { this.guardando = false; this.inscripcionForm.reset(); this.inscrito.emit(asistente); },
      error: () => { this.guardando = false; this.error = 'No se pudo inscribir al asistente. Revise los datos y la conexión.'; }
    });
  }
}
