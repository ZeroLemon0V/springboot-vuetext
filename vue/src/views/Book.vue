<template>
  <div style="padding: 10px">
<!--    功能区域-->
      <div style="margin:10px 0">
        <el-button type="primary" @click="add">新增</el-button>
        <el-popconfirm
          v-if="user.role === 1"
          title="确定删除吗？"
          @confirm="deleteBatch"
      >
        <template #reference>
          <el-button type="danger" >批量删除</el-button>
        </template>
      </el-popconfirm>
      </div>
<!--    搜索区域-->
    <div style="margin: 10px 0">
      <el-input v-model="search" placeholder="请输入关键字" style="width: 20%" clearable/>
      <el-button type="primary" style="margin-left: 5px" @click="load">查询</el-button>
    </div>

    <el-table :data="tableData" border stripe style="width: 100%" @selection-change="handleSelectionChange">
    <el-table-column type="selection" width="55" />
    <el-table-column prop="id" label="ID" sortable />
    <el-table-column prop="name" label="书名" />
    <el-table-column prop="price" label="价格" />
    <el-table-column prop="author" label="作者" />
    <el-table-column prop="createTime" label="创作时间" type="date" />
    <el-table-column  label="封面">
      <template #default="scope">
        <el-image
          style="width: 100px; height: 100px"
          :src="scope.row.cover"
          :preview-src-list="[scope.row.cover]"
           preview-teleported="true"
          :initial-index="4"
          fit="cover"/>
      </template>
    </el-table-column>
    <el-table-column label="操作" v-if="user.role===1">
      <template #default="scope">
        <el-button  size="mini" type="primary" @click="handleEdit(scope.row)" v-if="user.role===1">编辑</el-button>
        <el-popconfirm title="确认删除吗?" @confirm="handleDelete(scope.row.id)" v-if="user.role===1">
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

      <el-dialog v-model="dialogVisible" title="提示" width="30%">
        <el-from :model="form" label-width="120px">
          <el-form-item label="用户ID" style="display:none">
            <el-input v-model="form.userId" style="width:80%"/>
          </el-form-item>
          <el-form-item label="书名">
            <el-input v-model="form.name" style="width:80%"/>
          </el-form-item>
          <el-form-item label="价格">
            <el-input v-model="form.price" style="width:80%"/>
          </el-form-item>
          <el-form-item label="作者">
            <el-input v-model="form.author" style="width:80%"/>
          </el-form-item>
          <el-form-item label="创作时间">
            <el-date-picker v-model="form.createTime" value-format="YYYY-MM-DD" type="date" style="width:80%" clearable></el-date-picker>
           </el-form-item>
           <el-form-item label="封面">
             <el-upload
                 ref="upload"
                 action="http://localhost:9090/files/upload" :on-success="filesUploadSuccess">
                <el-button type="primary">点击上传</el-button>
                <img v-if="imageUrl" :src="imageUrl" class="avatar" />
                <el-icon v-else class="avatar-uploader-icon">
                  <Plus />
                </el-icon>
              </el-upload>
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
  </div>
</template>

<script>
import {ref} from "vue";
import request from "@/utils/request";

export default{
  name:'Book',
  components:{

  },
  data(){
    return{
      user:{},
      form:{},
      dialogVisible:false,
      search:'',
      currentPage:'1',
      pageSize:ref(10),
      total:10,
      tableData:[],
      ids:[],
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

    this.load()
  },
  methods: {
    deleteBatch(){
      if (!this.ids.length){
        this.$message.warning("请选择数据！")
        return
      }
      request.post("http://localhost:9090/book/deleteBatch",this.ids).then(res=>{
        if (res.code==='0'){
          this.$message.success("批量删除成功")
          this.load()
        }else {
          this.$message.error(res.msg)
        }
      })
    },
    handleSelectionChange(val){
      this.ids=val.map(v=>v.id)
      console.log(val.map(v=>v.id))
    },
    filesUploadSuccess(res){
      console.log(res)
      this.form.cover=res.data
    },
    save() {
      if (this.form.id){
        //更新
        request.put("http://localhost:9090/book",this.form).then(res=>{
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
        this.form.userId=this.user.id
        request.post("http://localhost:9090/book",this.form).then(res=>{
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
      request.get("http://localhost:9090/book",{
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
      this.$nextTick(()=>{
        if (this.$refs['upload']){
          this.$refs['upload'].clearFiles()
        }
      })

    },
    handleDelete(id){
      // alert('deldete')
      console.log(id)
      request.delete("http://localhost:9090/book/"+id).then(res=>{
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
      if (this.$refs['upload']){
        this.$refs['upload'].clearFiles()
      }
    },

  },



}
</script>

<style>
.el-form-item__label {
  text-align: justify;
}


</style>
