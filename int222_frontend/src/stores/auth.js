import { defineStore } from 'pinia'

export const useAuth = defineStore('auth', () => {
    const parseJwt = (token) => {
        let base64Url = token.split('.')[1];
        let base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
        let jsonPayload = decodeURIComponent(window.atob(base64).split('').map(function(c) {
            return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
        }).join(''));
        
        return JSON.parse(jsonPayload);
    }

    const getExpTime = (token) => {
        const expTime = parseJwt(token).exp
        const date = new Date(0)
        date.setUTCSeconds(expTime)
        return date
    }

    const isTokenExpired = () => {
        return getExpTime(localStorage.getItem("token")) < new Date();
    }

    const isRefreshTokenExpired = () => {
        return getExpTime(localStorage.getItem("refreshToken")) < new Date();
    }

    const isLoggedIn = () => {
        return localStorage.getItem("token") !== null && localStorage.getItem("refreshToken") !== null;
    }

    return { parseJwt, getExpTime, isTokenExpired, isRefreshTokenExpired, isLoggedIn }
})