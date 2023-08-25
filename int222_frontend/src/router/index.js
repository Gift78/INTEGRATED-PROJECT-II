import { createRouter, createWebHistory } from "vue-router";
import UserAnnouncement from "../views/UserAnnouncement.vue"
import UserAnnouncementDetail from "../views/UserAnnouncementDetail.vue"
import AdminAnnouncement from "../views/AdminAnnouncement.vue"
import AdminAnnouncementDetail from "../views/AdminAnnouncementDetail.vue"
import AddEditAnnouncement from "../views/AddEditAnnouncement.vue"
import UserListing from "../views/UserListing.vue"
import UserManagement from "../views/UserManagement.vue"
import NotFound from "../views/NotFound.vue"

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: "/",
      redirect: "/announcement",
    },
    {
      path: "/admin/announcement",
      name: "AdminAnnouncement",
      component: AdminAnnouncement,
    },
    {
      path: "/admin/announcement/:id",
      name: "AdminAnnouncementDetail",
      component: AdminAnnouncementDetail,
    },
    {
      path: "/admin/announcement/add",
      name: "AddAnnouncement",
      component: AddEditAnnouncement,
    },
    {
      path: "/admin/announcement/:id/edit",
      name: "EditAnnouncement",
      component: AddEditAnnouncement,
    },
    {
      path: "/announcement",
      name: "UserAnnouncement",
      component: UserAnnouncement,
    },
    {
      path: "/announcement/:id",
      name: "UserAnnouncementDetail",
      component: UserAnnouncementDetail,
    },
    {
      path: "/admin/user",
      name: "UserListing",
      component: UserListing,
    },
    {
      path: "/admin/user/:id/edit",
      name: "UserDetail",
      component: UserManagement,
    },
    {
      path: "/admin/user/add",
      name: "AddUser",
      component: UserManagement,
    },
    {
      path: "/:notfoundpath(.*)",
      name: "NotFound",
      component: NotFound,
    },
  ],
});

export default router;
