<template>
        <div class="content-box" 
            @wheel.prevent="pageWheel" 
            @touchstart="touchStart"
            @touchmove="touchMove"
            @touchend="touchEnd"
        ref="contentBox">
        <div class="content-top">
            <img src="/logo.png" alt="LOGO">
            <div class="content-nav">
                <div v-for="item in page">
                    <a :href="`#${item.id}`"
                        :class="pageInfo == item.id ? 'a-active' : ''"
                        @click.prevent="scrollToPage(item.id)"
                    >{{ item.title }}</a>
                </div>
            </div>
        </div>

        <!-- 这里新增加页面的时候需要再最外层的盒子添加一个类名“content-page”，设置对应的id -->
        <!-- 同时需要在script.js中page变量中添加对应的页面信息。 -->
        <page1></page1>
        <page2></page2>
        <page3></page3>
        <page4></page4>
        
        <Statement ref="statement"></Statement>
    </div>
</template>

<script setup>
    // 页面组件，注意要在page中添加对应页面信息，script.js中也要添加对应的页面信息
    import page1 from './components/Page1/Page1.vue';
    import page2 from './components/Page2/Page2.vue';
    import page3 from './components/Page3/Page3.vue';
    import page4 from './components/Page4/Page4.vue';

    import Statement from './components/Statement/Statement.vue';
    
    // 页面脚本
    import { scroll } from './script.js'

    // 初始化滚动逻辑
    const {
        page,
        contentBox,
        pageInfo,
        touchStart,
        touchMove,
        touchEnd,
        scrollToPage,
        statement
    } = scroll();

    defineExpose({
        statement
    });

</script>

<style scoped lang="scss">
    @import "./style.css";

    .content-page{
        width: 100%;
        height: 100%;
        min-width: 100%;
        // background-color: rgb(189, 189, 245);
        transition: all 1s; 
        scroll-snap-align: center; 
        font-size: 50px;
    }
    .content-box div:nth-child(2n+1){
        // filter: hue-rotate(45deg);
    }
</style>
