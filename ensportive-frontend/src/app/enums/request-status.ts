export enum RequestStatus {
  FILLED = 'FILLED',
  REQUESTED = 'REQUESTED',
  DENIED = 'DENIED',
}

export function requestStatusToString(status: string) {
  switch (status) {
    case RequestStatus.FILLED:
      return 'Preenchido';
    case RequestStatus.REQUESTED:
      return 'Solicitado';
    case RequestStatus.DENIED:
      return 'Negado';
    default:
      return 'Desconhecido';
  }
}
