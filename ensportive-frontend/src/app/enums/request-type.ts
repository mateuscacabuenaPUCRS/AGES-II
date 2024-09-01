export enum RequestType {
  REGISTER = 'REGISTER',
  ABSENCE = 'ABSENCE',
}

export function requestTypeToString(type: string) {
  switch (type) {
    case RequestType.REGISTER:
      return 'Cadastro';
    case RequestType.ABSENCE:
      return 'Ausência';
    default:
      return 'Desconhecido';
  }
}
