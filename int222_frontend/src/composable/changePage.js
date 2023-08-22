import router from '../router/index.js'; // ต้องใช้ {router} เป็นการเรียกจาก './router/index.js' ที่เราแก้ไขแล้ว

export const changePage = (name, id) => {
  if (id !== undefined) {
    router.push({ name: name, params: { id: id } });
  } else {
    router.push({ name: name });
  }
};