const formatDatetimeLocal = (datetime) => {
  if (!datetime) return null;

  const date = new Date(datetime);
  const options = {
    day: "numeric",
    month: "short",
    year: "numeric",
    hour: "numeric",
    minute: "numeric",
  };

  const formattedDate = date.toLocaleString("en-GB", options);
  return formattedDate;
};

const getFormattedDate = (date) => {
  if (date === null || date === undefined || date === '' || date === 'Invalid Date') {
      return '';
  }

  date = new Date(date)

  return `${date.getFullYear()}-${(date.getMonth() + 1).toString().padStart(2, '0')}-${date.getDate().toString().padStart(2, '0')}`
}

const getFormattedTime = (date) => {
  if (date === null || date === undefined || date === '' || date === 'Invalid Date') {
      return '';
  }

  date = new Date(date)

  return `${date.getHours().toString().padStart(2, '0')}:${date.getMinutes().toString().padStart(2, '0')}`
}

const getFormattedDateTimeISO = (date, time) => {
  if (!date || !time) {
      return null;
  }
  const includeDateTime = date + ' , ' + time

  const dateObj = new Date(includeDateTime).toISOString().slice(0, -5) + "Z";
  return dateObj
}

export { formatDatetimeLocal, getFormattedDate, getFormattedTime, getFormattedDateTimeISO };
