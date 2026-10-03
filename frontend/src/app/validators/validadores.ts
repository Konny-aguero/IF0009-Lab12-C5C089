import { ValidatorFn } from '@angular/forms';
export const validarRangoFechas: ValidatorFn = control => {
  const inicio = control.get('fechaInicio')?.value;
  const fin = control.get('fechaFin')?.value;
  return inicio && fin && fin < inicio ? {fechasInvalidas: true} : null;
};
export const mayorDe18: ValidatorFn = control => {
  if (control.value === null || control.value === '') return null;
  return Number.isInteger(control.value) && control.value > 18 ? null : {mayorDe18: true};
};
