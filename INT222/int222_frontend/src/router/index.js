import { createRouter, createWebHistory } from "vue-router";
import UserAnnouncement from "../views/UserAnnouncement.vue"
import UserAnnouncementDetail from "../views/UserAnnouncementDetail.vue"
import AdminAnnouncement from "../views/AdminAnnouncement.vue"
import AdminAnnouncementDetail from "../views/AdminAnnouncementDetail.vue"
import AnnouncementManagement from "../views/AnnouncementManagement.vue"
import UserListing from "../views/UserListing.vue"
import UserManagement from "../views/UserManagement.vue"
import NotFound from "../views/NotFound.vue"
import UserMatchPassword from "../views/UserMatchPassword.vue"
import UserLogin from "../views/UserLogin.vue"
import UserLogout from "../views/UserLogout.vue"
import Unsubscribe from "../views/Unsubscribe.vue"
import { useAuth } from '../stores/auth';
import { getNewToken } from '../composable/getData';
import Swal from 'sweetalert2';


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
      component: AnnouncementManagement,
    },
    {
      path: "/admin/announcement/:id/edit",
      name: "EditAnnouncement",
      component: AnnouncementManagement,
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
      path: "/admin/user/match",
      name: "UserMatchPassword",
      component: UserMatchPassword,
    },
    {
      path: "/:notfoundpath(.*)",
      name: "NotFound",
      component: NotFound,
    },
    {
      path: "/login",
      name: "UserLogin",
      component: UserLogin,
    },
    {
      path: "/logout",
      name: "UserLogout",
      component: UserLogout,
    },
    // Unsubscribe
    {
      path: "/unsubscribe",
      name: "Unsubscribe",
      component: Unsubscribe,
    },
  ],
});

router.beforeEach((to, from, next) => {
  const auth = useAuth();
  const { isTokenExpired, isRefreshTokenExpired, isLoggedIn, getRole } = auth;
  if (to.name === 'UserAnnouncement' || to.name === 'UserAnnouncementDetail' || to.name === 'UserLogout' || to.name === 'Unsubscribe') {
    next();
  } else if (!isLoggedIn() && to.name !== 'UserLogin') {
    next({ name: 'UserAnnouncement' });
  } else if (to.name !== 'UserLogin' && isTokenExpired()) {
    if (isRefreshTokenExpired()) {
      localStorage.clear();
      next({ name: 'UserAnnouncement' });
    } else {
      getNewToken()
        .then(() => {
          next({ name: to.name });
        })
        .catch(() => {
          localStorage.clear();
          next({ name: 'UserAnnouncement' });
        });
    }
  } else if (isLoggedIn() && getRole() !== 'admin' && (to.name == 'UserListing' || to.name == 'UserMatchPassword')) {
    Swal.fire({
      icon: 'error',
      title: '403 Forbidden',
      text: "You don't have permission to access this page.",
      confirmButtonColor: '#155e75',
      confirmButtonText: 'Go back',
    }).then((result) => {
      if (result.isConfirmed) {
        next({ name: 'AdminAnnouncement' });
      }
    })
  } else {
    next();
  }
});


export default router;