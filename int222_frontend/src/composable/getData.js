const getAllData = async (mode) => {
  try {
    const res = await fetch(
      import.meta.env.VITE_ROOT_API + `/api/announcements?mode=${mode}`, {
      method: 'GET',
      headers: {
        'Authorization': `Bearer ${localStorage.getItem("token")}`
      }
    })
    if (res.ok) {
      const data = await res.json();
      return data;
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
      "/api/announcements/" + id + "?count=" + isCount, {
      method: 'GET',
      headers: {
        'Authorization': `Bearer ${localStorage.getItem("token")}`
      }
    })
    if (res.ok) {
      const data = await res.json();
      return data;
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
    }
  } catch (err) {
    console.log(err);
  }
};
const getCategoryById = async (id) => {
  try {
    const res = await fetch(
      import.meta.env.VITE_ROOT_API + "/api/category/" + id, {
      headers: {
        'Authorization': `Bearer ${localStorage.getItem("token")}`
      }
    })
    if (res.ok) {
      const category = await res.json();
      return category;
    }
  } catch (err) {
    console.log(err);
  }
};

const getDataByPage = async (mode, page, size) => {
  try {
    const res = await fetch(
      import.meta.env.VITE_ROOT_API +
      `/api/announcements/pages?mode=${mode}&page=${page}&size=${size}`)
    if (res.ok) {
      const data = await res.json();
      return data;
    }
  } catch (err) {
    console.log(err);
  }
};

const getAnnoucementPageByCategoryId = async (mode, page, size, catId) => {
  try {
    const res = await fetch(
      import.meta.env.VITE_ROOT_API +
      `/api/announcements/pages?mode=${mode}&page=${page}&size=${size}&category=${catId}`)
    if (res.ok) {
      const data = await res.json();
      return data;
    }
  } catch (err) {
    console.log(err);
  }
};

const getAllUsers = async () => {
  try {
    const res = await fetch(import.meta.env.VITE_ROOT_API + `/api/users`, {
      method: 'GET',
      headers: {
        'Authorization': `Bearer ${localStorage.getItem("token")}`
      }
    })
    if (res.ok) {
      const users = await res.json();
      return users;
    } 
  } catch (err) {
    console.log(err);
  }
};

const getUserById = async (id) => {
  try {
    const res = await fetch(
      import.meta.env.VITE_ROOT_API + "/api/users/" + id, {
      method: 'GET',
      headers: {
        'Authorization': `Bearer ${localStorage.getItem("token")}`
      }
    })
    if (res.ok) {
      const data = await res.json();
      return data;
    }
  } catch (err) {
    console.log(err);
  }
};

const getNewToken = async () => {
  console.log("refreshing token");

  const res = await fetch(
    import.meta.env.VITE_ROOT_API + "/api/token", {
    method: "GET",
    headers: {
      "Authorization": `Bearer ${localStorage.getItem("refreshToken")}}`,
    },
  });

  if (res.ok) {
    const data = await res.json();
    localStorage.setItem("token", data.token);
    return data;
  }
}

export {
  getAllData,
  getDataById,
  getAllCategories,
  getCategoryById,
  getDataByPage,
  getAnnoucementPageByCategoryId,
  getAllUsers,
  getUserById,
  getNewToken
};
