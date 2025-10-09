<template>
    <router-view></router-view>
</template>

<script setup>
    import { request } from './api/request.js'
    import router from './router/index.js'

    const CheckServer = async () => {
        try{
            let res = await request('/api/serveronline', 'GET' , null , null)
        }catch(err){
            console.log(err)
            // 跳转到error页面
            router.push('/error')
        }
    }

    // CheckServer()
</script>

<style scoped>
    *{
        margin: 0;
        padding: 0;
    }
    .web{
        width: 100vw;
        height: 100vh;
    }
</style>
