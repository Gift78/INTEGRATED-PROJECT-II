const getAllData = async (mode) => {
  try {
    const res = await fetch(
      import.meta.env.VITE_ROOT_API + `/api/announcements?mode=${mode}`
    );
    if (res.ok) {
      const data = await res.json();
      return data;
    } else if (res.status === 404) {
      throw new Error("404 Not Found");
    } else if (res.status === 500) {
      throw new Error("500 Internal Server Error");
    } else if (res.status === 503) {
      throw new Error("503 Service Unavailable");
    } else {
      throw new Error("Something went wrong.");
    }
  } catch (err) {
    console.log(err);
  }
};

const getDataById = async (id, isCount) => {
  if (isCount === undefined) {
    isCount = false;
  }

  try {
    const res = await fetch(
      import.meta.env.VITE_ROOT_API +
        "/api/announcements/" +
        id +
        "?count=" +
        isCount
    );
    if (res.ok) {
      const data = await res.json();
      return data;
    } else {
      throw new Error("Something went wrong.");
    }
  } catch (err) {
    console.log(err);
  }
};

const getAllCategories = async () => {
  try {
    const res = await fetch(import.meta.env.VITE_ROOT_API + `/api/category`);
    if (res.ok) {
      const categories = await res.json();
      return categories;
    } else if (res.status === 404) {
      throw new Error("404 Not Found");
    } else if (res.status === 500) {
      throw new Error("500 Internal Server Error");
    } else if (res.status === 503) {
      throw new Error("503 Service Unavailable");
    } else {
      throw new Error("Something went wrong.");
    }
  } catch (err) {
    console.log(err);
  }
};
const getCategoryById = async (id) => {
  try {
    const res = await fetch(
      import.meta.env.VITE_ROOT_API + "/api/category/" + id
    );
    if (res.ok) {
      const category = await res.json();
      return category;
    } else if (res.status === 404) {
      throw new Error("404 Not Found");
    } else if (res.status === 500) {
      throw new Error("500 Internal Server Error");
    } else if (res.status === 503) {
      throw new Error("503 Service Unavailable");
    } else {
      throw new Error("Something went wrong.");
    }
  } catch (err) {
    console.log(err);
  }
};
const getDataByPage = async (mode, page, size) => {
  try {
    const res = await fetch(
      import.meta.env.VITE_ROOT_API +
        `/api/announcements/pages?mode=${mode}&page=${page}&size=${size}`
    );
    if (res.ok) {
      const data = await res.json();
      return data;
    } else if (res.status === 404) {
      throw new Error("404 Not Found");
    } else if (res.status === 500) {
      throw new Error("500 Internal Server Error");
    } else if (res.status === 503) {
      throw new Error("503 Service Unavailable");
    } else {
      throw new Error("Something went wrong.");
    }
  } catch (err) {
    console.log(err);
  }
};

const getAnnoucementPageByCategoryId = async (mode, page, size, catId) => {
  try {
    const res = await fetch(
      import.meta.env.VITE_ROOT_API +
        `/api/announcements/pages?mode=${mode}&page=${page}&size=${size}&category=${catId}`
    );
    if (res.ok) {
      const data = await res.json();
      return data;
    } else if (res.status === 404) {
      throw new Error("404 Not Found");
    } else if (res.status === 500) {
      throw new Error("500 Internal Server Error");
    } else if (res.status === 503) {
      throw new Error("503 Service Unavailable");
    } else {
      throw new Error("Something went wrong.");
    }
  } catch (err) {
    console.log(err);
  }
};
export {
  getAllData,
  getDataById,
  getAllCategories,
  getCategoryById,
  getDataByPage,
  getAnnoucementPageByCategoryId,
};
