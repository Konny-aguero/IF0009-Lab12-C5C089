import { ValidatorFn } from '@angular/forms';
export const validarRangoFechas: ValidatorFn = control => {
  const inicio = control.get('fechaInicio')?.value;
  const fin = control.get('fechaFin')?.value;
  return inicio && fin && fin < inicio ? {fechasInvalidas: true} : null;
};
