<template>
  <div style="width:100%;height:100vh;background-color:darkslateblue;overflow: hidden">
    <div style="width: 400px;margin: 150px auto">
      <div style="color:#ccc;font-size: 30px;text-align: center;padding: 30px 0">欢迎登陆</div>
        <el-form ref="form" :model="form" :rules="rules" size="normal">
          <el-form-item prop="username">
            <el-input prefix-icon='User' v-model="form.username" />
          </el-form-item>
          <el-form-item prop="password">
            <el-input prefix-icon="Lock" v-model="form.password" show-password />
          </el-form-item>
          <el-form-item>
            <div style="display: flex">
              <el-input prefix-icon="Key" v-model="form.validCode" style="width: 60%;" placeholder="请输入验证码"></el-input>
              <ValidCode  @input="createValidCode" />
            </div>
          </el-form-item>
<!--          <el-form-item>-->
<!--            <el-radio v-model="form.role" :label="1">管理员</el-radio>-->
<!--            <el-radio v-model="form.role" :label="2">普通用户</el-radio>-->
<!--          </el-form-item>-->
          <el-form-item>
            <el-button style="width: 100%" type="primary" @click="login" >登 录</el-button>
          </el-form-item>
          <el-form-item>
            <el-button style="width: 100%" type="primary" @click="$router.push('/register')" >注 册</el-button>
          </el-form-item>
        </el-form>
    </div>
  </div>
</template>

<script>
import {ref} from "vue";
import request from "@/utils/request";
import ValidCode from "@/components/ValidCode";

export default {
  name: "Login",
  components: {
    ValidCode,
  },
  data(){
    return{
      form:{},
      rules:{
        username: [
          { required: true, message: '请输入用户名', trigger: 'blur' },
        ],
        password: [
          { required: true, message: '请输入密码', trigger: 'blur' },
        ],
      },
      validCode: ''
    }
  },
  methods:{
    createValidCode(data) {
      this.validCode = data
    },
    login(){
      this.$refs['form'].validate((valid)=>{
        if (valid){
          if (!this.form.validCode) {
            this.$message.error("请填写验证码")
            return
          }
          if(this.form.validCode.toLowerCase() !== this.validCode.toLowerCase()) {
            this.$message.error("验证码错误")
            return
          }
          request.post("http://localhost:9090/user/login",this.form).then(res=>{
            if (res.code==='0'){
              this.$message({
                type:"success",
                message:"登录成功"
              })
              sessionStorage.setItem("user", JSON.stringify(res.data))  //缓存用户信息
              this.$router.push("/")  //登录成功后进行页面跳转
            }else {
              this.$message({
                type:"error",
                message:res.msg
              })
            }
          })
        }
      })
    }

  },
  setup(){
    return{

    }
  }
}
</script>

<style scoped>

</style>