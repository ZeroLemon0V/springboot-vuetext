import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import 'dayjs/locale/zh-cn'
import locale from 'element-plus/lib/locale/lang/zh-cn'


import '@/assets/css/global.css'

import * as ElementPlusIconsVue from '@element-plus/icons'

const app = createApp(App);

// 屏蔽错误信息
// app.config.errorHandler = () => null;
// 屏蔽警告信息
app.config.warnHandler = () => null;


for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
    app.component(key, component)
}

app.use(store).use(router).use(ElementPlus,{locale,size:'small'}).mount('#app')