<template>
    <input 
        type="checkbox" 
        class="btn-check" 
        :checked="checkBoxstate" 
        @change="checkBoxstate = true"
    >
    <div class="content">
        <div class="top-box">
            <img src="/logo.png" alt="LOGO">
            <button class="menu-btn">
                <span></span>
            </button>
        </div>

        <div class="nav-box">
            <!-- ChangeSection方法是用于标识当前模块 -->
            <div class="nav-grid">
                <router-link @click="ChangeSection('index')" to="/web/index">
                    <div :style="Section === 'index'? 'color: black;' : 'color: gray;'" class="content-div">首页</div>
                </router-link>
                <router-link @click="ChangeSection('assets')" to="/web/assets">
                    <div :style="Section === 'assets'? 'color: black;' : 'color: gray;'" class="content-div">资源获取</div>
                </router-link>
                <router-link @click="ChangeSection('join')" to="/web/join">
                    <div :style="Section === 'join'? 'color: black;' : 'color: gray;'" class="content-div">加入我们</div>
                </router-link>
                <router-link @click="ChangeSection('personal')" to="/web/personal">
                    <div :style="Section === 'personal'? 'color: black;' : 'color: gray;'" class="content-div">个人中心</div>
                </router-link>
            </div>
        </div>

        <!-- 遮罩层，点击遮罩层关闭菜单 -->
        <div class="content-box-before" @click="checkBoxstate = false"></div>

        <!-- 这里新增加模块的时候需要再最外层的盒子添加一个类名“content-box” -->
           <!-- 使用 router-view 显示嵌套路由 -->
        <div class="content-box">
            <router-view v-slot="{ Component }">
                <transition name="fade-slide" mode="out-in">
                    <div>
                        <component :is="Component" />
                    </div>
                </transition>
            </router-view>
        </div>
    </div>
</template>

<script setup>
    // 引入状态管理
    import { ref , watch ,onMounted } from 'vue';
    // 引入封装好的请求方法
    import { request } from '../../api/request';
    import { useRoute } from 'vue-router';
    
    const checkBoxstate = ref(false);
    const route = useRoute();
    const Section = ref('index');

    // 关闭菜单时保持路由同步
    watch(() => checkBoxstate.value, (newVal) => {
        if (!newVal) {
            // 菜单关闭时确保路由状态正确
            const currentPath = route.path;
        }
    });

    // 切换板块
    const ChangeSection = (section) => {
        console.log(checkBoxstate.value);
        Section.value = section;
        // 添加延迟触发，保证动画不会冲突
        setTimeout(() => {
            checkBoxstate.value = false;
        }, 500);
    }

    // 生命周期
    onMounted(() => {
        // 测试接口
        testApi();
    })

    // 接口测试
    const testApi = async() => {
        let data = {email: '12345@qq.com'}
        let data2 = {email: '2743887852@qq.com'}
        let heasers = {
            'Content-Type': 'application/json',
            'Authorization': '123456'
        }
        let heasers1 = {
            'Content-Type': 'application/json',
            'Authorization': '1234562'
        }
        let res = null;
        res = await request('/api/usertest', 'GET', data, null);
        console.log(res);
        res = await request('/api/test', 'GET', data, null);
        console.log(res);
        res = await request('/api/get', 'GET', data, null);
        console.log(res);
        res = await request('/api/settype1', 'POST', {}, null);
        console.log(res);
        res = await request('/api/settype2', 'POST', {}, heasers);
        console.log(res);
        res = await request('/api/settype2', 'POST', {}, heasers1);
        console.log(res);
        res = await request('/api/sendemail', 'GET', data2, null);
        console.log(res);
    }
</script>

<style scoped lang="scss">
    @import "./style.css";

    .content-box{
        position: relative;
        width: 100%;
        height: 100%;
        background-color: rgb(255, 255, 255);
        transition: all 0.5s;
        z-index: 3;
    }
    .content-box div{
        width: 100%;
        height: 100%;
    }

    .content-box-before{
        display: none;
        position: absolute;
        bottom: 0;
        width: 80%;
        height: 50%;
        background-color: rgb(255, 255, 255, 0.1);
        transition: all 0.5s;
        z-index: 5;
    }

    // 设置组件切换动画
    .fade-slide-enter-active,
    .fade-slide-leave-active {
        transition: all 0.3s ease;
    }

    .fade-slide-enter-from {
        opacity: 0;
        transform: translateY(0px);
    }

    .fade-slide-leave-to {
        opacity: 1;
        transform: translateY(0px);
    }

</style>
