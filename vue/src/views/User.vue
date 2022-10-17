<template>
  <div style="padding: 10px">
<!--    功能区域-->
      <div style="margin:10px 0">
        <el-button type="primary" @click="add">新增</el-button>
      </div>
<!--    搜索区域-->
    <div style="margin: 10px 0">
      <el-input v-model="search" placeholder="请输入关键字" style="width: 20%" clearable/>
      <el-button type="primary" style="margin-left: 5px" @click="load">查询</el-button>
    </div>

    <el-table :data="tableData" border stripe style="width: 100%">
    <el-table-column prop="id" label="ID" sortable />
    <el-table-column prop="username" label="用户名" />
    <el-table-column prop="nickName" label="昵称" />
    <el-table-column prop="age" label="年龄" />
    <el-table-column prop="sex" label="性别" />
    <el-table-column prop="address" label="地址" />
    <el-table-column label="角色" >
      <template #default="scope">
        <span v-if="scope.row.role ===1">管理员</span>
        <span v-if="scope.row.role ===2">普通用户</span>
      </template>
    </el-table-column>
    <el-table-column label="操作">
      <template #default="scope">
        <el-button  size="mini" type="success" plain @click="showBooks(scope.row.bookList)">查看图书列表</el-button>
        <el-button  size="mini" type="primary" plain @click="handleEdit(scope.row)" v-if="user.role === 1">编辑</el-button>
        <el-popconfirm title="确认删除吗?" @confirm="handleDelete(scope.row.id)" v-if="user.role === 1">
          <template #reference>
            <el-button size="mini" type="danger">删除</el-button>
          </template>
        </el-popconfirm>
      </template>
    </el-table-column>
    </el-table>
    <div style="margin: 10px 0">
      <el-pagination
          v-model:currentPage="currentPage"
          v-model:page-size="pageSize"
          :page-sizes="[5, 10, 20]"
          :small="small"
          :disabled="disabled"
          :background="background"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"/>
    </div>

      <el-dialog v-model="bookVis" title="用户拥有的图书列表" width="30%">
        <el-table :data="bookList" stripe border>
          <el-table-column prop="id" label="ID"></el-table-column>
          <el-table-column prop="name" label="名称"></el-table-column>
          <el-table-column prop="price" label="价格"></el-table-column>
        </el-table>
      </el-dialog>

      <el-dialog v-model="dialogVisible" title="提示" width="30%">
        <el-from :model="form" label-width="120px">
          <el-form-item label="用户名">
            <el-input v-model="form.username" style="width:80%"/>
          </el-form-item>
          <el-form-item label="昵称">
            <el-input v-model="form.nickName" style="width:80%"/>
          </el-form-item>
          <el-form-item label="年龄">
            <el-input v-model="form.age" style="width:80%"/>
          </el-form-item>
          <el-form-item label="性别">
            <div class="mb-2 flex items-center text-sm">
              <el-radio-group v-model="form.sex" class="ml-4">
                <el-radio label="男" size="large">男</el-radio>
                <el-radio label="女" size="large">女</el-radio>
                <el-radio label="未知" size="large">未知</el-radio>
              </el-radio-group>
            </div>
          </el-form-item>
          <el-form-item label="地址">
            <el-input type="textarea" v-model="form.address" style="width:80%"/>
          </el-form-item>

        </el-from>
        <template #footer>
          <span class="dialog-footer">
            <el-button @click="dialogVisible = false">取消</el-button>
            <el-button type="primary" @click="save">确定</el-button>
          </span>
        </template>
      </el-dialog>
    </div>
</template>

<script>
import {ref} from "vue";
import request from "@/utils/request";

export default{
  name:'User',
  components:{

  },
  data(){
    return{
      user:{},
      form:{},
      dialogVisible:false,
      bookVis:false,
      search:'',
      currentPage:'1',
      pageSize:ref(10),
      total:10,
      tableData:[],
      bookList:[],
    }
  },
  created(){
    this.load()
  },
  methods: {
    showBooks(books){
      this.bookList=books
      this.bookVis=true
    },
    save() {
      if (this.form.id){
        //更新
        request.put("http://localhost:9090/user",this.form).then(res=>{
          console.log(res)
          if (res.code==='0'){
            this.$message({
              type:"success",
              message:"更新成功"
            })
          }else {
            this.$message({
              type:"error",
              message:res.msg
            })
          }
          this.load() //刷新表格数据
          this.dialogVisible=false //关闭弹窗
        })
      }else {
        //新增
        request.post("http://localhost:9090/user",this.form).then(res=>{
          console.log(res)
          if (res.code==='0'){
            this.$message({
              type:"success",
              message:"新增成功"
            })
            }else {
              this.$message({
                type:"error",
                message:res.msg
              })
            }
          this.load() //刷新表格数据
          this.dialogVisible=false //关闭弹窗
          })
      }

    },
    load(){
      request.get("http://localhost:9090/user",{
        params:{
          pageNum:this.currentPage,
          pageSize:this.pageSize,
          search:this.search
        }
      }).then(res=>{
        console.log(res)
        this.tableData=res.data.records
        this.total=res.data.total
      })
    },
    handleEdit(row){
      // alert('click')
      this.form=JSON.parse(JSON.stringify(row))
      this.dialogVisible=true
      // console.log(row)
    },
    handleDelete(id){
      // alert('deldete')
      console.log(id)
      request.delete("http://localhost:9090/user/"+id).then(res=>{
        console.log(res)
        if (res.code==='0'){
          this.$message({
            type:"success",
            message:"删除成功"
          })
        }else {
          this.$message({
            type:"error",
            message:res.msg
          })
        }
        this.load() //刷新表格数据
      })
    },
    handleSizeChange(){//改变当前每页的个数触发
      this.load()
    },
    handleCurrentChange(){//改变当前页码触发
      this.load()
    },
    add(){
      this.dialogVisible=true
      this.form={}
    },

  }


}
</script>

<style>
.el-form-item__label {
  text-align: justify;
}


</style>
