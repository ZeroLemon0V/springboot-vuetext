<template>
  <div>
    <el-menu style="width: 200px;min-height: calc(100vh - 50px)" :default-active="path" router  class="el-menu-vertical-demo" @open="handleOpen" @close="handleClose">
      <el-sub-menu index="1" >
        <template #title>
          <el-icon>
            <location/>
          </el-icon>
          <span>系统管理</span>
        </template>
        <el-menu-item-group>
          <el-menu-item index="/user">用户管理</el-menu-item>
          <el-menu-item index="1">item two</el-menu-item>
        </el-menu-item-group>
      </el-sub-menu>
      <el-menu-item index="/book">
        <el-icon>
          <icon-menu/>
        </el-icon>
        <span>书籍管理</span>
      </el-menu-item>
      <el-menu-item index="/news">
        <el-icon>
          <setting/>
        </el-icon>
        <span>新闻管理</span>
      </el-menu-item>
    </el-menu>
  </div>
</template>

<script>

import {
  Document,
  Menu as IconMenu,
  Location,
  Setting,
} from '@element-plus/icons-vue'
import request from "@/utils/request";

export default {
  name: "Aside",
  data(){
    return{
      user:{},
      path:this.$route.path,
    }
  },
  created(){
    let userStr=sessionStorage.getItem("user")||"{}"
    this.user=JSON.parse(userStr)
    request.get("http://localhost:9090/user/"+this.user.id).then(res=>{
      if (res.code==='0'){
        this.user=res.data
      }
    })
  },
  components:{
    Document,
    IconMenu,
    Location,
    Setting,
  },

}
</script>

<style scoped>

</style>