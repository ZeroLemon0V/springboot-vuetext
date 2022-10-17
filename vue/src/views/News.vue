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
    <el-table-column prop="title" label="标题" />
    <el-table-column prop="author" label="作者" />
    <el-table-column prop="time" label="创作时间" type="date" />
      <el-table-column label="操作">
        <template #default="scope">
          <el-button  size="mini" type="primary" @click="details(scope.row)" >详情</el-button>
          <el-button  size="mini" type="primary" @click="handleEdit(scope.row)" v-if="user.role === 1" >编辑</el-button>
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

    <el-dialog v-model="dialogVisible" title="提示" width="50%">
        <el-from :model="form" label-width="120px">
          <el-form-item label="标题">
            <el-input v-model="form.title" style="width:50%"/>
          </el-form-item>

          <div id="div1"></div>
<!--          <el-form-item label="内容">-->
<!--            <el-input v-model="form.content" style="width:80%"/>-->
<!--          </el-form-item>-->


        </el-from>
        <template #footer>
          <span class="dialog-footer">
            <el-button @click="dialogVisible = false">取消</el-button>
            <el-button type="primary" @click="save">确定</el-button>
          </span>
        </template>
      </el-dialog>

      <el-dialog v-model="vis" title="详情" width="50%">
        <el-card>
          <div v-html="detail.content" style="min-height: 100px"></div>
        </el-card>
      </el-dialog>
  </div>
</template>

<script>
// import '@wangeditor/editor/dist/css/style.css' // 引入 css

import { onBeforeUnmount, ref, shallowRef, onMounted } from 'vue'
// import { Editor, Toolbar } from '@wangeditor/editor-for-vue'

import E from 'wangeditor';
import request from "@/utils/request";

let editor


export default{
  name:'News',
  components:{},
  data(){
    return{
      form:{},
      dialogVisible:false,
      search:'',
      currentPage:'1',
      pageSize:ref(10),
      total:10,
      tableData:[],
      xiangqing:{},
      vis:false,
      detail: {},
      user:{},

    }
  },
  created(){

    this.load()
  },
  methods: {

    details(row){
      this.detail= row
      this.vis=true
    },
    filesUploadSuccess(res){
      console.log(res)
      this.form.cover=res.data
    },

    save() {
      this.form.content=editor.txt.html()  //获取编辑器里面的值，然后赋值到实体当中

      if (this.form.id){
        //更新
        request.put("http://localhost:9090/news",this.form).then(res=>{
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
        let userStr=sessionStorage.getItem("user")||"{}"
        let user=JSON.parse(userStr)
        this.form.author=user.nickName

        request.post("http://localhost:9090/news",this.form).then(res=>{
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
      request.get("http://localhost:9090/news",{
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
        //关联弹窗里面的div、new一个editor对象
        if (!editor){
          editor=new E('#div1')


          editor.config.uploadImgServer = 'http://localhost:9090/files/editor/upload'
          editor.config.uploadFileName = "file"

          editor.create()
        }
        editor.txt.html(row.content)
      })
    },
    handleDelete(id){
      // alert('deldete')
      console.log(id)
      request.delete("http://localhost:9090/news/"+id).then(res=>{
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

      this.$nextTick(()=>{
        //关联弹窗里面的div、new一个editor对象
        editor=new E('#div1')


        editor.config.uploadImgServer = 'http://localhost:9090/files/editor/upload'
        editor.config.uploadFileName = "file"

        editor.create()
      })

    },

  },



}
</script>

<style>
.el-form-item__label {
  text-align: justify;
}


</style>
