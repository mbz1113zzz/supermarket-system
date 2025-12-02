<template>
  <div class="app-container">
    <el-card class="table-container">
      <div class="filter-container">
        <el-form :inline="true" :model="queryParams" size="small">
          <el-form-item label="Product Name">
            <el-input
              v-model="queryParams.name"
              placeholder="Enter product name"
              clearable
              style="width: 200px"
              @keyup.enter.native="handleQuery"
            />
          </el-form-item>
          <el-form-item label="Category">
            <el-select v-model="queryParams.category" placeholder="Select category" clearable style="width: 150px">
              <el-option label="Beverages" value="饮料" />
              <el-option label="Snacks" value="零食" />
              <el-option label="Convenience Food" value="方便食品" />
              <el-option label="Candy" value="糖果" />
              <el-option label="Dairy" value="乳制品" />
              <el-option label="Daily Necessities" value="日用品" />
            </el-select>
          </el-form-item>
          <el-form-item label="Stock Alert">
            <el-select v-model="queryParams.stockWarning" placeholder="Select" clearable style="width: 120px">
              <el-option label="Low stock" value="low" />
              <el-option label="Sufficient stock" value="normal" />
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" icon="el-icon-search" @click="handleQuery">Search</el-button>
            <el-button icon="el-icon-refresh" @click="resetQuery">Reset</el-button>
            <el-button type="success" icon="el-icon-plus" @click="handleAdd">Add Product</el-button>
            <el-button type="warning" icon="el-icon-download" @click="handleExport">Export</el-button>
          </el-form-item>
        </el-form>
      </div>

      <el-table
        v-loading="loading"
        :data="productList"
        border
        style="width: 100%"
        size="small"
      >
        <el-table-column type="index" label="No." width="80" align="center" />
        <el-table-column prop="name" label="Product Name" min-width="180" />
        <el-table-column prop="category" label="Category" width="100" />
        <el-table-column prop="price" label="Price (CNY)" width="100" align="right">
          <template slot-scope="scope">
            <span class="price-value">{{ (scope.row.price / 100).toFixed(2) }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="integral" label="Points" width="80" align="right" />
        <el-table-column prop="stock" label="Stock" width="100" align="right">
          <template slot-scope="scope">
            <span :class="getStockClass(scope.row.stock)">{{ scope.row.stock }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="unit" label="Unit" width="80" align="center">
          <template slot-scope="scope">
            {{ formatUnit(scope.row.unit) }}
          </template>
        </el-table-column>
        <el-table-column prop="time" label="Created At" width="160" />
        <el-table-column label="Stock Status" width="100" align="center">
          <template slot-scope="scope">
            <el-tag :type="getStockStatusType(scope.row.stock)" size="mini">
              {{ getStockStatusText(scope.row.stock) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="Actions" width="200" fixed="right">
          <template slot-scope="scope">
            <el-button type="text" size="small" icon="el-icon-edit" @click="handleEdit(scope.row)">Edit</el-button>
            <el-button type="text" size="small" icon="el-icon-plus" @click="handleStockIn(scope.row)">Restock</el-button>
            <el-button
              type="text"
              size="small"
              icon="el-icon-delete"
              style="color: #F56C6C"
              @click="handleDelete(scope.row)"
            >
              Delete
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-pagination
        background
        :current-page="pagination.pageIndex"
        :page-sizes="[10, 20, 50, 100]"
        :page-size="pagination.pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="pagination.pageTotal"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </el-card>

    <!-- add/edit product dialog -->
    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="600px" :close-on-click-modal="false">
      <el-form ref="productForm" :model="productForm" :rules="productRules" label-width="100px" size="small">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="Product Name" prop="name">
              <el-input v-model="productForm.name" placeholder="Enter product name" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="Category" prop="category">
              <el-select v-model="productForm.category" placeholder="Select category" style="width: 100%">
                <el-option label="Beverages" value="饮料" />
                <el-option label="Snacks" value="零食" />
                <el-option label="Convenience Food" value="方便食品" />
                <el-option label="Candy" value="糖果" />
                <el-option label="Dairy" value="乳制品" />
                <el-option label="Daily Necessities" value="日用品" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="Price (CNY)" prop="price">
              <el-input-number
                v-model="productForm.price"
                :min="0.01"
                :max="999999"
                :precision="2"
                placeholder="Enter price"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="Points" prop="integral">
              <el-input-number
                v-model="productForm.integral"
                :min="0"
                :max="9999"
                placeholder="Enter points"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="Stock Quantity" prop="stock">
              <el-input-number
                v-model="productForm.stock"
                :min="0"
                :max="99999"
                placeholder="Enter stock quantity"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="Unit" prop="unit">
              <el-select v-model="productForm.unit" placeholder="Select unit" style="width: 100%">
                <el-option label="Pieces (个)" value="个" />
                <el-option label="Bottles (瓶)" value="瓶" />
                <el-option label="Bags (包)" value="包" />
                <el-option label="Boxes (盒)" value="盒" />
                <el-option label="Sticks (支)" value="支" />
                <el-option label="Bars (条)" value="条" />
                <el-option label="Pouches (袋)" value="袋" />
                <el-option label="Cans (罐)" value="罐" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">Cancel</el-button>
        <el-button type="primary" @click="submitForm" :loading="submitting">Confirm</el-button>
      </div>
    </el-dialog>

    <!-- restock dialog -->
    <el-dialog title="Restock Product" :visible.sync="stockInDialogVisible" width="400px">
      <el-form ref="stockInForm" :model="stockInForm" :rules="stockInRules" label-width="100px" size="small">
        <el-form-item label="Product Name">
          <span>{{ currentProduct.name }}</span>
        </el-form-item>
        <el-form-item label="Current Stock">
          <span>{{ currentProduct.stock }} {{ formatUnit(currentProduct.unit) }}</span>
        </el-form-item>
        <el-form-item label="Quantity to Add" prop="quantity">
          <el-input-number
            v-model="stockInForm.quantity"
            :min="1"
            :max="9999"
            placeholder="Enter quantity to add"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="Remark" prop="remark">
          <el-input
            v-model="stockInForm.remark"
            type="textarea"
            :rows="3"
            placeholder="Enter remark (optional)"
          />
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="stockInDialogVisible = false">Cancel</el-button>
        <el-button type="primary" @click="submitStockIn" :loading="stockInSubmitting">Confirm</el-button>
      </div>
    </el-dialog>

    <!-- inventory stats card -->
    <el-row :gutter="20" style="margin-top: 20px;">
      <el-col :xs="24" :sm="24" :md="6" :lg="6" :xl="6">
        <el-card class="stats-card">
          <div class="stats-content">
            <div class="stats-icon" style="background-color: #409EFF;">
              <i class="el-icon-goods"></i>
            </div>
            <div class="stats-info">
              <div class="stats-number">{{ totalProducts }}</div>
              <div class="stats-label">Total Products</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :xs="24" :sm="24" :md="6" :lg="6" :xl="6">
        <el-card class="stats-card">
          <div class="stats-content">
            <div class="stats-icon" style="background-color: #67C23A;">
              <i class="el-icon-box"></i>
            </div>
            <div class="stats-info">
              <div class="stats-number">{{ totalStock }}</div>
              <div class="stats-label">Total Stock</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :xs="24" :sm="24" :md="6" :lg="6" :xl="6">
        <el-card class="stats-card">
          <div class="stats-content">
            <div class="stats-icon" style="background-color: #E6A23C;">
              <i class="el-icon-warning"></i>
            </div>
            <div class="stats-info">
              <div class="stats-number">{{ lowStockCount }}</div>
              <div class="stats-label">Low Stock Items</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :xs="24" :sm="24" :md="6" :lg="6" :xl="6">
        <el-card class="stats-card">
          <div class="stats-content">
            <div class="stats-icon" style="background-color: #F56C6C;">
              <i class="el-icon-sold-out"></i>
            </div>
            <div class="stats-info">
              <div class="stats-number">{{ outOfStockCount }}</div>
              <div class="stats-label">Out of Stock</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { getProductList, addProduct, updateProduct, deleteProduct, updateStock } from '@/api/product'

export default {
  name: 'ProductList',
  data() {
    return {
      loading: false,
      submitting: false,
      stockInSubmitting: false,
      productList: [],
      queryParams: {
        name: '',
        category: '',
        stockWarning: ''
      },
      pagination: {
        pageIndex: 1,
        pageSize: 10,
        pageTotal: 0
      },
      dialogVisible: false,
      stockInDialogVisible: false,
      isEdit: false,
      dialogTitle: '',
      currentProduct: {},
      productForm: {
        name: 'New Product 001', // default name
        price: 9.99,
        integral: 1,
        category: 'Snacks',
        stock: 50,
        unit: '个'
      },
      stockInForm: {
        quantity: 1,
        remark: ''
      },
      productRules: {
        name: [
          { required: true, message: 'Please enter a product name', trigger: 'blur' },
          { min: 2, max: 50, message: 'Length must be between 2 and 50 characters', trigger: 'blur' }
        ],
        price: [
          { required: true, message: 'Please enter a price', trigger: 'blur' },
          { type: 'number', min: 0.01, message: 'Price must be greater than 0', trigger: 'blur' }
        ],
        integral: [
          { required: true, message: 'Please enter points', trigger: 'blur' },
          { type: 'number', min: 0, message: 'Points cannot be negative', trigger: 'blur' }
        ],
        category: [
          { required: true, message: 'Please select a category', trigger: 'change' }
        ],
        stock: [
          { required: true, message: 'Please enter a stock quantity', trigger: 'blur' },
          { type: 'number', min: 0, message: 'Stock cannot be negative', trigger: 'blur' }
        ],
        unit: [
          { required: true, message: 'Please select a unit', trigger: 'change' }
        ]
      },
      stockInRules: {
        quantity: [
          { required: true, message: 'Please enter a quantity', trigger: 'blur' },
          { type: 'number', min: 1, message: 'Quantity must be greater than 0', trigger: 'blur' }
        ]
      }
    }
  },
  computed: {
    totalProducts() {
      return this.pagination.pageTotal
    },
    totalStock() {
      return this.productList.reduce((sum, product) => sum + product.stock, 0)
    },
    lowStockCount() {
      return this.productList.filter(product => product.stock > 0 && product.stock < 50).length
    },
    outOfStockCount() {
      return this.productList.filter(product => product.stock === 0).length
    }
  },
  created() {
    this.fetchProductList()
  },
  methods: {
    async fetchProductList() {
      this.loading = true
      try {
        const params = {
          pageIndex: this.pagination.pageIndex,
          pageSize: this.pagination.pageSize,
          ...this.queryParams
        }
        const response = await getProductList(params)
        if (response.data) {
          this.productList = response.data.list
          this.pagination.pageTotal = response.data.pageTotal
        }
      } catch (error) {
        this.$message.error('Failed to retrieve product list')
        console.error('Failed to retrieve product list:', error)
      } finally {
        this.loading = false
      }
    },

    handleQuery() {
      this.pagination.pageIndex = 1
      this.fetchProductList()
    },

    resetQuery() {
      this.queryParams = {
        name: '',
        category: '',
        stockWarning: ''
      }
      this.handleQuery()
    },

    handleExport() {
      this.$message.info('Export feature under development...')
    },

    handleAdd() {
      this.isEdit = false
      this.dialogTitle = 'Add Product'
      this.dialogVisible = true
      this.resetForm()
    },

    handleEdit(row) {
      this.isEdit = true
      this.dialogTitle = 'Edit Product'
      this.dialogVisible = true
      this.productForm = {
        productid: row.productid,
        name: row.name,
        price: row.price / 100,
        integral: row.integral,
        category: row.category,
        stock: row.stock,
        unit: row.unit
      }
    },

    handleStockIn(row) {
      this.currentProduct = row
      this.stockInForm = {
        quantity: 1,
        remark: ''
      }
      this.stockInDialogVisible = true
    },

    handleDelete(row) {
      this.$confirm(`Delete product ${row.name}? This action cannot be undone.`, 'Warning', {
        confirmButtonText: 'Confirm',
        cancelButtonText: 'Cancel',
        type: 'warning'
      }).then(async () => {
        try {
          await deleteProduct(row.productid)
          this.$message.success('Deleted successfully')
          this.fetchProductList()
        } catch (error) {
          this.$message.error('Deletion failed')
        }
      }).catch(() => {
        this.$message.info('Deletion canceled')
      })
    },

    submitForm() {
      this.$refs.productForm.validate(async (valid) => {
        if (valid) {
          this.submitting = true
          try {
            const data = {
              ...this.productForm,
              price: Math.round(this.productForm.price * 100) // Convert to cents
            }
            
            if (this.isEdit) {
              await updateProduct(data)
              this.$message.success('Product updated successfully')
            } else {
              await addProduct(data)
              this.$message.success('Product added successfully')
            }

            this.dialogVisible = false
            this.fetchProductList()
          } catch (error) {
            this.$message.error(this.isEdit ? 'Update failed' : 'Creation failed')
          } finally {
            this.submitting = false
          }
        }
      })
    },

    submitStockIn() {
      this.$refs.stockInForm.validate(async (valid) => {
        if (valid) {
          this.stockInSubmitting = true
          try {
            const newStock = this.currentProduct.stock + this.stockInForm.quantity
            await updateStock(this.currentProduct.productid, newStock)

            this.$message.success('Restock successful')
            this.stockInDialogVisible = false
            this.fetchProductList()
          } catch (error) {
            this.$message.error('Restock failed')
          } finally {
            this.stockInSubmitting = false
          }
        }
      })
    },

    resetForm() {
      this.productForm = {
        name: '',
        price: 0,
        integral: 0,
        category: '',
        stock: 100,
        unit: '个'
      }
      if (this.$refs.productForm) {
        this.$refs.productForm.clearValidate()
      }
    },

    getStockClass(stock) {
      if (stock === 0) return 'stock-out'
      if (stock < 50) return 'stock-low'
      return 'stock-normal'
    },

    getStockStatusType(stock) {
      if (stock === 0) return 'danger'
      if (stock < 50) return 'warning'
      return 'success'
    },

    getStockStatusText(stock) {
      if (stock === 0) return 'Out of stock'
      if (stock < 50) return 'Low stock'
      return 'In stock'
    },

    formatUnit(unit) {
      const map = {
        '个': 'pcs',
        '瓶': 'bottles',
        '包': 'bags',
        '盒': 'boxes',
        '支': 'sticks',
        '条': 'bars',
        '袋': 'pouches',
        '罐': 'cans'
      }
      return map[unit] ? `${map[unit]} (${unit})` : unit || '-'
    },

    handleSizeChange(val) {
      this.pagination.pageSize = val
      this.pagination.pageIndex = 1
      this.fetchProductList()
    },

    handleCurrentChange(val) {
      this.pagination.pageIndex = val
      this.fetchProductList()
    }
  }
}
</script>

<style lang="scss" scoped>
.filter-container {
  margin-bottom: 20px;
}

.price-value {
  color: #E6A23C;
  font-weight: bold;
}

.stock-normal {
  color: #67C23A;
  font-weight: bold;
}

.stock-low {
  color: #E6A23C;
  font-weight: bold;
}

.stock-out {
  color: #F56C6C;
  font-weight: bold;
}

.dialog-footer {
  text-align: right;
}

.el-pagination {
  margin-top: 20px;
  text-align: right;
}

.stats-card {
  margin-bottom: 20px;

  .stats-content {
    display: flex;
    align-items: center;
    padding: 10px 0;

    .stats-icon {
      width: 60px;
      height: 60px;
      border-radius: 50%;
      display: flex;
      align-items: center;
      justify-content: center;
      margin-right: 15px;

      i {
        font-size: 24px;
        color: white;
      }
    }

    .stats-info {
      flex: 1;

      .stats-number {
        font-size: 24px;
        font-weight: bold;
        color: #303133;
        margin-bottom: 5px;
      }

      .stats-label {
        font-size: 14px;
        color: #909399;
      }
    }
  }
}
</style>