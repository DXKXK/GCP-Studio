import { ref, onMounted, onUnmounted} from 'vue';

export function scroll(){
    // 导航栏渲染
    const page = [
        {id:'page1' ,title:'首页'},
        {id:'page2' ,title:'关于我们'},
        {id:'page3' ,title:'工作室环境'},
        {id:'page4' ,title:'加入我们'},
    ]
    
    // 页面滚动使用的
    const pageElement= ref([])
    const contentBox = ref(null);
    const statement = ref(null);
    const pageInfo = ref('page1');
    const allowScroll = ref(true);
    // 适配触摸滑动
    const touchStartY = ref(0);
    const touchEndY = ref(0);
    // 记录页尾状态,false:未显示，true:显示
    let statementState = false;
    
    // 控制页面平滑滚动(触摸滚动)
    const touchStart = (e) => {
        // 触摸开始
        e.preventDefault();
        touchStartY.value = e.touches[0].clientY;
    };
    const touchMove = (e) => {
        // 触摸移动
        e.preventDefault();
        touchEndY.value = e.touches[0].clientY;
    };
    
    // 触摸结束事件
    const touchEnd = () => {
        // 计算滑动步长
        const distance = touchStartY.value - touchEndY.value;
        // console.log(distance)
        pageWheel({deltaY: distance});
        // 重置触摸位置
        touchStartY.value = 0;
        touchEndY.value = 0;
    };
    
    // 控制页面平滑滚动(a标签跳转)
    const scrollToPage = (targetPage) => {
        // console.log(targetPage)
        const container = contentBox.value;
        const targetElement = document.getElementById(targetPage);
        
        if (!targetElement) return;
    
        container.scrollTo({
            top: targetElement.offsetTop,
            behavior: 'smooth'
        });
    }
    
    // 控制页面平滑滚动(鼠标滚轮)
    const pageWheel = (e) => {
        // console.log(e)
        if(e.preventDefault === 'function'){
            e.preventDefault();
        }
        const container = contentBox.value;
        // console.log(container.clientHeight)
        if (!container) return;
    
        let containerHeight = 0;
        // 判断是否是最后一页
        if(pageInfo.value == page[page.length - 1].id){
            // 是最后一页，判断滚动方向
            if(Math.sign(e.deltaY) > 0){
                containerHeight = statement.value.$el.clientHeight;
                statementState = true;
            }else{
                // 向上滚动，判断页尾是否显示
                if(statementState){
                    // 页尾显示
                    containerHeight = statement.value.$el.clientHeight;
                    // 这里设置延迟的原因是因为页面滚动到页尾部分后，再次往上滚动的时候会跨页面，因此需要添加延迟，保证滚动限制不会被提前释放
                    setTimeout(() => {
                        statementState = false;
                    }, 500);
                }else{
                    // 页尾未显示
                    containerHeight = container.clientHeight;
                }
            }
        }else{
            // 不是最后一页，照常滚动
            containerHeight = container.clientHeight;
        }
        const delta = Math.sign(e.deltaY) * containerHeight;
    
        container.addEventListener('scrollend', NowPage, { once: true });
    
        if(allowScroll.value){
            // 执行平滑滚动
            container.scrollBy({
                top: delta,
                behavior: 'smooth'
            });
            allowScroll.value = false;
        }
    
        setTimeout(() => {
            allowScroll.value = true;
        }, 500);
    }
    
    // 监听事件
    const NowPage= () => {
        const container = contentBox.value;
        const midPoint = container.clientHeight / 2;
        
        // 遍历所有页面元素
        pageElement.value.forEach((element, index) => {
            const rect = element.getBoundingClientRect();
            if (rect.top <= midPoint && rect.bottom >= midPoint) {
                pageInfo.value = `page${index + 1}`;
            }
        });
        // console.log('当前页面：', pageInfo.value);
    }
    
    // 生命周期
    onMounted(() => {
        
        // 获取页面滚动元素
        if (contentBox.value) {
            contentBox.value.addEventListener('wheel', pageWheel, { passive: false })
        }
    
        // 获取页尾DOM元素
        if (statement.value) {
            // console.log(statement.value.$el.scrollWidth);
        }
    
        // 获取所有页面DOM元素
        pageElement.value = Array.from(
            contentBox.value.querySelectorAll('.content-page')
        );

        // 监听滚动行为，当页面不存在滚动行为时则触发页面更新
        contentBox.value.addEventListener('scroll', () => {
            NowPage();
        });
        
        // 添加滚动监听
        contentBox.value.addEventListener('scrollend', NowPage);
    
    });
    
    onUnmounted(() => {
        if (contentBox.value) {
            contentBox.value.removeEventListener('scrollend', NowPage);
        }
    });

    return {
        page,
        contentBox,
        pageInfo,
        touchStart,
        touchMove,
        touchEnd,
        scrollToPage,
        statement
    }
}
