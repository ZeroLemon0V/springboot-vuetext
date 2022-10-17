<template>
  <div>
    <el-card style="width: 40%;margin: 10px">
      <el-form ref="form" :model="form" lable-width="80px">
        <el-form-item label="用户名">
          <el-input v-model="form.username" disabled />
        </el-form-item>
        <el-form-item label="昵称">
          <el-input v-model="form.nickName"/>
        </el-form-item>
        <el-form-item label="年龄">
          <el-input v-model="form.age"/>
        </el-form-item>
        <el-form-item label="性别">
          <el-input v-model="form.sex"/>
        </el-form-item>
        <el-form-item label="地址">
          <el-input v-model="form.address"/>
        </el-form-item>
        <el-form-item label="密码 ">
          <el-input v-model="form.password" show-password/>
        </el-form-item>
      </el-form>
      <div style="text-align:center">
        <el-button type="primary" @click="update" router>保存</el-button>
      </div>
      <div>

      </div>
    </el-card>
  </div>
</template>

<script>
import request from "@/utils/request";

export default {
  name: "Person",
  data(){
    return{
      form:{},
      path:this.$route.path
    }
  },
  created(){
    let str = sessionStorage.getItem("user") || "{}"
    this.form = JSON.parse(str)
  },
  methods:{
    update(){
      request.put("http://localhost:9090/user",this.form).then(res=>{
        console.log(res)
        if (res.code==='0'){
          this.$message({
            type:"success",
            message:"更新成功"
          })
          sessionStorage.setItem("user",JSON.stringify(this.form))
          this.$emit("userInfo")

          // this.$router.push("/")
          this.$router.go(0)
        }else {
          this.$message({
            type:"error",
            message:res.msg
          })
        }
      })
    }
  }
}

</script>

<style scoped>

</style>