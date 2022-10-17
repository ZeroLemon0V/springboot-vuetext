<template>
  <div style="height: 50px;line-height: 50px;border-bottom: 1px solid #ccc;display: flex">
    <div style="width: 200px; padding-left: 30px;font-weight: bold;color: dodgerblue">后台管理</div>
    <div style="flex: 1"></div>
    <div style="width: 100px;padding-top: 20px">
      <el-dropdown>
        <span class="el-dropdown-link">
<!--            {{user.nickName}}-->
<!--            {{user.nickName}}-->
<!--            {{JSON.parse( sessionStorage.getItem("user")).nickName}}-->
<!--            {{NickName}}-->
            {{user.nickName}}
          <el-icon class="el-icon--right">
            <arrow-down/>
          </el-icon>
        </span>
        <template #dropdown>
          <el-dropdown-menu>
            <el-dropdown-item @click="$router.push('/person')">个人信息</el-dropdown-item>
            <el-dropdown-item @click="$router.push('/login')">退出系统</el-dropdown-item>
          </el-dropdown-menu>
        </template>
      </el-dropdown>
    </div>
  </div>

</template>

<script>
import {ArrowDown} from '@element-plus/icons-vue'
import {reactive} from "vue";
import request from "@/utils/request";

// var user=JSON.parse( sessionStorage.getItem("user"));
// let NickName=JSON.parse(sessionStorage.getItem("user")||"{}")

export default {
  name: "Header",
  props:['user'],
  components: {
    ArrowDown
  },
  data() {
    return {
      user:{},
      // NickName
    }
  },
  created(){
    let userStr=sessionStorage.getItem("user")||"{}"
    this.user=JSON.parse(userStr)
    request.get("http://localhost:9090/user/"+this.user).then(res=>{
      if (res.code==='0'){
        this.user=res.data
      }
    })
  },


}

</script>

<style scoped>
.example-showcase .el-dropdown-link {
  cursor: pointer;
  color: var(--el-color-primary);
  display: flex;
  align-items: center;
}
</style>