<template>
  <div class="app-container">
    <!-- query filters -->
    <el-card class="filter-container">
      <div class="filter-form">
        <el-form :inline="true" :model="searchForm" size="small">
          <el-form-item label="Member ID">
            <el-input v-model="searchForm.memberId" placeholder="Enter member ID" clearable style="width: 150px;" @keyup.enter.native="handleSearch" />
          </el-form-item>
          <el-form-item label="Behavior Type">
            <el-select v-model="searchForm.behaviorType" placeholder="Select behavior" clearable style="width: 120px;">
              <el-option label="Browse" :value="1" />
              <el-option label="Add to Cart" :value="2" />
              <el-option label="Purchase" :value="3" />
              <el-option label="Favorite" :value="4" />
            </el-select>
          </el-form-item>
          <el-form-item label="Product Category">
            <el-select v-model="searchForm.productCategory" placeholder="Select category" clearable style="width: 150px;">
              <el-option label="Fruits" value="水果" />
              <el-option label="Vegetables" value="蔬菜" />
              <el-option label="Meat" value="肉类" />
              <el-option label="Dairy" value="乳制品" />
              <el-option label="Grains & Oil" value="粮油" />
              <el-option label="Snacks" value="零食" />
              <el-option label="Beverages" value="饮料" />
            </el-select>
          </el-form-item>
          <el-form-item label="Date Range">
            <el-date-picker
              v-model="dateRange"
              type="daterange"
              range-separator="to"
              start-placeholder="Start date"
              end-placeholder="End date"
              value-format="yyyy-MM-dd"
              style="width: 350px;"
              @change="handleDateChange"
            />
          </el-form-item>
          <el-form-item>
            <el-button type="primary" icon="el-icon-search" @click="handleSearch">Search</el-button>
            <el-button icon="el-icon-refresh" @click="resetSearch">Reset</el-button>
          </el-form-item>
        </el-form>
      </div>
    </el-card>

    <!-- data overview -->
    <el-row :gutter="20" style="margin-bottom: 20px;">
      <el-col :span="6">
        <el-card class="stats-card">
          <div class="stats-item">
            <div class="stats-title">Total Behaviors</div>
            <div class="stats-value">{{ behaviorStats.total || 0 }}</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stats-card">
          <div class="stats-item">
            <div class="stats-title">Purchase Conversion</div>
            <div class="stats-value">{{ conversionRate }}%</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stats-card">
          <div class="stats-item">
            <div class="stats-title">Active Members</div>
            <div class="stats-value">{{ activeMembers }}</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stats-card">
          <div class="stats-item">
            <div class="stats-title">Avg. Order Value</div>
            <div class="stats-value">¥{{ avgOrderValue }}</div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- charts area -->
    <el-row :gutter="20" style="margin-bottom: 20px;">
      <!-- behavior type pie chart -->
      <el-col :span="12">
        <el-card>
          <div slot="header">
            <span>Behavior Type Distribution</span>
          </div>
          <div id="behaviorPieChart" style="width: 100%; height: 300px;"></div>
        </el-card>
      </el-col>

      <!-- category sales bar chart -->
      <el-col :span="12">
        <el-card>
          <div slot="header">
            <span>Category Sales Analysis</span>
          </div>
          <div id="categoryBarChart" style="width: 100%; height: 300px;"></div>
        </el-card>
      </el-col>
    </el-row>

    <!-- hot products and member spending ranking -->
    <el-row :gutter="20">
      <!-- hot products ranking -->
      <el-col :span="12">
        <el-card>
          <div slot="header" class="clearfix">
            <span>Top Products</span>
            <el-button style="float: right; padding: 3px 0" type="text" icon="el-icon-refresh" @click="fetchHotProducts">Refresh</el-button>
          </div>
          <el-table :data="hotProducts" size="small" style="width: 100%;">
            <el-table-column prop="rank" label="Rank" width="60" align="center" />
            <el-table-column prop="productName" label="Product" min-width="120" show-overflow-tooltip />
            <el-table-column prop="sales" label="Units Sold" width="80" align="right" />
            <el-table-column prop="revenue" label="Revenue" width="100" align="right">
              <template slot-scope="scope">
                ¥{{ scope.row.revenue.toFixed(0) }}
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-col>

      <!-- member spending ranking -->
      <el-col :span="12">
        <el-card>
          <div slot="header" class="clearfix">
            <span>Top Member Spend</span>
            <el-button style="float: right; padding: 3px 0" type="text" icon="el-icon-refresh" @click="fetchMemberConsumption">Refresh</el-button>
          </div>
          <el-table :data="memberConsumption" size="small" style="width: 100%;">
            <el-table-column prop="rank" label="Rank" width="60" align="center" />
            <el-table-column prop="memberName" label="Member" min-width="100" show-overflow-tooltip />
            <el-table-column prop="consumption" label="Spend" width="100" align="right">
              <template slot-scope="scope">
                ¥{{ scope.row.consumption.toFixed(0) }}
              </template>
            </el-table-column>
            <el-table-column prop="orderCount" label="Orders" width="80" align="right" />
          </el-table>
        </el-card>
      </el-col>
    </el-row>

    <!-- customer behavior details table -->
    <el-card style="margin-top: 20px;">
      <div slot="header">
        <span>Customer Behavior Details</span>
      </div>
      <el-table
        v-loading="tableLoading"
        :data="customerBehaviors"
        border
        style="width: 100%"
        size="small"
      >
        <el-table-column prop="memberId" label="Member ID" width="100" />
        <el-table-column prop="behaviorType" label="Behavior" width="80">
          <template slot-scope="scope">
            <el-tag :type="getBehaviorTypeTag(scope.row.behaviorType)" size="small">
              {{ getBehaviorTypeText(scope.row.behaviorType) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="productName" label="Product" min-width="150" show-overflow-tooltip />
        <el-table-column prop="productCategory" label="Category" width="100">
          <template slot-scope="scope">
            {{ getCategoryLabel(scope.row.productCategory) }}
          </template>
        </el-table-column>
        <el-table-column prop="orderAmount" label="Amount" width="100" align="right" v-if="showOrderAmount">
          <template slot-scope="scope">
            <span v-if="scope.row.orderAmount">¥{{ scope.row.orderAmount.toFixed(2) }}</span>
            <span v-else>-</span>
          </template>
        </el-table-column>
        <el-table-column prop="behaviorTime" label="Time" width="160" />
        <el-table-column prop="sessionId" label="Session ID" width="120" show-overflow-tooltip />
      </el-table>

      <!-- pagination -->
      <div class="pagination-container" v-if="customerBehaviors.length > 0">
        <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="pagination.currentPage"
          :page-sizes="[10, 20, 50, 100]"
          :page-size="pagination.pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="pagination.total"
        />
      </div>
    </el-card>
  </div>
</template>

<script>
import * as echarts from 'echarts'
import {
  getCustomerBehaviors,
  getBehaviorStats,
  getCategoryStats,
  getHotProducts,
  getMemberConsumption
} from '@/api/marketing'

export default {
  name: 'CustomerAnalytics',
  data() {
    return {
      loading: false,
      tableLoading: false,
      dateRange: [],
      searchForm: {
        memberId: '',
        behaviorType: null,
        productCategory: '',
        startDate: '',
        endDate: ''
      },
      customerBehaviors: [],
      behaviorStats: {},
      categoryStats: [],
      hotProducts: [],
      memberConsumption: [],
      pagination: {
        currentPage: 1,
        pageSize: 20,
        total: 0
      }
    }
  },
  computed: {
    conversionRate() {
      if (!this.behaviorStats.data || this.behaviorStats.data.length < 2) return 0
      const viewData = this.behaviorStats.data.find(item => item.behaviorType === 1) || { count: 0 }
      const purchaseData = this.behaviorStats.data.find(item => item.behaviorType === 3) || { count: 0 }
      return viewData.count > 0 ? ((purchaseData.count / viewData.count) * 100).toFixed(2) : 0
    },
    activeMembers() {
      // mock data, should get from API in real scenario
      // return Math.floor(Math.random() * 500) + 100
      return 0
    },
    avgOrderValue() {
      if (!this.memberConsumption.length) return '0.00'
      const total = this.memberConsumption.reduce((sum, item) => sum + item.consumption, 0)
      const avg = total / this.memberConsumption.length
      return avg.toFixed(2)
    },
    showOrderAmount() {
      return this.searchForm.behaviorType === 3 || this.searchForm.behaviorType === null
    }
  },
  created() {
    this.initCharts()
    this.fetchData()
  },
  mounted() {
    this.renderCharts()
  },
  methods: {
    async fetchData() {
      this.loading = true
      this.tableLoading = true

      try {
        // fetch all data in parallel - using real API
        const [behaviorStatsRes, categoryStatsRes, hotProductsRes, memberConsumptionRes] = await Promise.all([
          getBehaviorStats(this.searchForm),
          getCategoryStats(this.searchForm),
          getHotProducts({ ...this.searchForm, limit: 10 }),
          getMemberConsumption({ ...this.searchForm, limit: 10 })
        ])

        if (behaviorStatsRes.status === 200) {
          // backend returns array, wrap it in object for later logic
          this.behaviorStats = { data: behaviorStatsRes.data }
          this.calculateTotal()
        }

        if (categoryStatsRes.status === 200) {
          this.categoryStats = categoryStatsRes.data
        }

        if (hotProductsRes.status === 200) {
          this.hotProducts = hotProductsRes.data
        }

        if (memberConsumptionRes.status === 200) {
          this.memberConsumption = memberConsumptionRes.data
        }

        // render charts after data loaded
        this.$nextTick(() => {
          this.renderCharts()
        })

        // fetch customer behavior details
        this.fetchCustomerBehaviors()

      } catch (error) {
        console.error('Failed to load analytics data:', error)
        this.$message.error('Failed to load analytics data')
      } finally {
        this.loading = false
        this.tableLoading = false
      }
    },

    async fetchCustomerBehaviors() {
      this.tableLoading = true
      try {
        // call real API to get customer behavior data
        const response = await getCustomerBehaviors({
          memberId: this.searchForm.memberId,
          behaviorType: this.searchForm.behaviorType,
          productCategory: this.searchForm.productCategory,
          startDate: this.searchForm.startDate,
          endDate: this.searchForm.endDate
        })
        if (response.status === 200) {
          this.customerBehaviors = response.data || []
          this.pagination.total = this.customerBehaviors.length
        } else {
          this.$message.error(response.msg || 'Failed to fetch customer behaviors')
          this.customerBehaviors = []
        }
      } catch (error) {
        console.error('Failed to fetch customer behaviors:', error)
        this.$message.error('Failed to fetch customer behaviors')
        this.customerBehaviors = []
      } finally {
        this.tableLoading = false
      }
    },

    /*
    generateMockBehaviors() {
      const behaviors = []
      const members = ['M001', 'M002', 'M003', 'M004', 'M005']
      const products = ['Apple', 'Banana', 'Milk', 'Bread', 'Eggs', 'Yogurt', 'Orange', 'Tomato']
      const categories = ['水果', '水果', '乳制品', '粮油', '蛋类', '乳制品', '水果', '蔬菜']

      for (let i = 0; i < 50; i++) {
        const memberIndex = Math.floor(Math.random() * members.length)
        const productIndex = Math.floor(Math.random() * products.length)
        const behaviorType = Math.floor(Math.random() * 4) + 1

        behaviors.push({
          memberId: members[memberIndex],
          behaviorType: behaviorType,
          productName: products[productIndex],
          productCategory: categories[productIndex],
          orderAmount: behaviorType === 3 ? (Math.random() * 200 + 10) : null,
          sessionId: 'S' + Math.random().toString(36).substr(2, 9),
          behaviorTime: this.getRandomDateTime()
        })
      }

      return behaviors.sort((a, b) => new Date(b.behaviorTime) - new Date(a.behaviorTime))
    },

    getRandomDateTime() {
      const now = new Date()
      const randomDays = Math.floor(Math.random() * 30)
      const randomHours = Math.floor(Math.random() * 24)
      const randomMinutes = Math.floor(Math.random() * 60)

      const date = new Date(now)
      date.setDate(date.getDate() - randomDays)
      date.setHours(randomHours, randomMinutes, 0, 0)

      return date.toISOString().replace('T', ' ').substr(0, 19)
    },
    */

    calculateTotal() {
      if (this.behaviorStats.data && this.behaviorStats.data.length > 0) {
        this.behaviorStats.total = this.behaviorStats.data.reduce((sum, item) => sum + item.count, 0)
      } else {
        this.behaviorStats.total = 0
      }
    },

    handleDateChange(dates) {
      if (dates && dates.length === 2) {
        this.searchForm.startDate = dates[0]
        this.searchForm.endDate = dates[1]
      } else {
        this.searchForm.startDate = ''
        this.searchForm.endDate = ''
      }
    },

    handleSearch() {
      this.pagination.currentPage = 1
      this.fetchData()
      this.renderCharts()
    },

    resetSearch() {
      this.searchForm = {
        memberId: '',
        behaviorType: null,
        productCategory: '',
        startDate: '',
        endDate: ''
      }
      this.dateRange = []
      this.pagination.currentPage = 1
      this.fetchData()
      this.renderCharts()
    },

    handleSizeChange(val) {
      this.pagination.pageSize = val
      this.fetchCustomerBehaviors()
    },

    handleCurrentChange(val) {
      this.pagination.currentPage = val
      this.fetchCustomerBehaviors()
    },

    async fetchHotProducts() {
      try {
        const response = await getHotProducts({ ...this.searchForm, limit: 10 })
        if (response.status === 200) {
          this.hotProducts = response.data || []
        }
      } catch (error) {
        console.error('Failed to fetch hot products:', error)
        this.$message.error('Failed to fetch hot products')
      }
    },

    async fetchMemberConsumption() {
      try {
        const response = await getMemberConsumption({ ...this.searchForm, limit: 10 })
        if (response.status === 200) {
          this.memberConsumption = response.data || []
        }
      } catch (error) {
        console.error('Failed to fetch member consumption ranking:', error)
        this.$message.error('Failed to fetch member consumption ranking')
      }
    },

    initCharts() {
      this.$nextTick(() => {
        this.behaviorPieChart = echarts.init(document.getElementById('behaviorPieChart'))
        this.categoryBarChart = echarts.init(document.getElementById('categoryBarChart'))

        // listen for window resize
        window.addEventListener('resize', () => {
          this.behaviorPieChart && this.behaviorPieChart.resize()
          this.categoryBarChart && this.categoryBarChart.resize()
        })
      })
    },

    renderCharts() {
      this.renderBehaviorPieChart()
      this.renderCategoryBarChart()
    },

    renderBehaviorPieChart() {
      if (!this.behaviorPieChart || !this.behaviorStats.data) return

      const data = this.behaviorStats.data.map(item => ({
        name: item.typeName,
        value: item.count,
        percentage: item.percentage
      }))

      const option = {
        tooltip: {
          trigger: 'item',
          formatter: '{a} <br/>{b}: {c} ({d}%)'
        },
        legend: {
          bottom: '5%',
          left: 'center'
        },
        series: [
          {
            name: 'Behavior Types',
            type: 'pie',
            radius: ['40%', '70%'],
            avoidLabelOverlap: false,
            itemStyle: {
              borderRadius: 10,
              borderColor: '#fff',
              borderWidth: 2
            },
            label: {
              show: false,
              position: 'center'
            },
            emphasis: {
              label: {
                show: true,
                fontSize: '18',
                fontWeight: 'bold'
              }
            },
            labelLine: {
              show: false
            },
            data: data
          }
        ]
      }

      this.behaviorPieChart.setOption(option)
    },

    renderCategoryBarChart() {
      if (!this.categoryBarChart || !this.categoryStats.length) return

      const categories = this.categoryStats.map(item => this.getCategoryLabel(item.category))
      const amounts = this.categoryStats.map(item => item.totalAmount)

      const option = {
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'shadow'
          }
        },
        grid: {
          left: '3%',
          right: '4%',
          bottom: '3%',
          containLabel: true
        },
        xAxis: {
          type: 'value'
        },
        yAxis: {
          type: 'category',
          data: categories
        },
        series: [
          {
            name: 'Sales',
            type: 'bar',
            data: amounts,
            itemStyle: {
              color: '#409EFF'
            }
          }
        ]
      }

      this.categoryBarChart.setOption(option)
    },

    getBehaviorTypeTag(type) {
      const typeMap = {
        1: 'info',      // browse
        2: 'warning',   // add to cart
        3: 'success',   // purchase
        4: 'danger'     // favorite
      }
      return typeMap[type] || 'info'
    },

    getBehaviorTypeText(type) {
      const typeMap = {
        1: 'Browse',
        2: 'Add to Cart',
        3: 'Purchase',
        4: 'Favorite'
      }
      return typeMap[type] || 'Unknown'
    },

    getCategoryLabel(category) {
      const map = {
        '水果': 'Fruits',
        '蔬菜': 'Vegetables',
        '肉类': 'Meat',
        '乳制品': 'Dairy',
        '粮油': 'Grains & Oil',
        '零食': 'Snacks',
        '饮料': 'Beverages',
        '蛋类': 'Eggs'
      }
      return map[category] || category
    }
  },

  beforeDestroy() {
    // cleanup chart instances
    if (this.behaviorPieChart) {
      this.behaviorPieChart.dispose()
    }
    if (this.categoryBarChart) {
      this.categoryBarChart.dispose()
    }
    // remove event listeners
    window.removeEventListener('resize', this.handleResize)
  }
}
</script>

<style scoped>
.filter-container {
  margin-bottom: 20px;
}

.stats-card {
  text-align: center;
}

.stats-item {
  padding: 10px 0;
}

.stats-title {
  font-size: 14px;
  color: #909399;
  margin-bottom: 8px;
}

.stats-value {
  font-size: 24px;
  font-weight: bold;
  color: #409EFF;
}

.pagination-container {
  margin-top: 20px;
  text-align: right;
}

.clearfix:before,
.clearfix:after {
  display: table;
  content: "";
}
.clearfix:after {
  clear: both
}
</style>