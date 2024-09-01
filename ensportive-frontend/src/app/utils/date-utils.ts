export const dateToYYYYMMDD = (date: Date): string => {
  const day = date.getDate().toString().padStart(2, '0');
  const month = (date.getMonth() + 1).toString().padStart(2, '0');
  const year = date.getFullYear();
  const result = `${year}-${month}-${day}`;
  return result;
};
