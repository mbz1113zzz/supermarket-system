<template>
  <div class="dashboard-container">
    <!-- statistics cards section -->
    <el-row :gutter="20" class="stats-cards">
      <el-col :xs="24" :sm="12" :md="6" :lg="6" :xl="6">
        <el-card class="stat-card">
          <div class="stat-content">
            <div class="stat-icon member-icon">
              <i class="el-icon-user"></i>
            </div>
            <div class="stat-info">
              <div class="stat-number">{{ statisticData.memberNum || 0 }}</div>
              <div class="stat-title">Total Members</div>
            </div>
          </div>
        </el-card>
      </el-col>

      <el-col :xs="24" :sm="12" :md="6" :lg="6" :xl="6">
        <el-card class="stat-card">
          <div class="stat-content">
            <div class="stat-icon card-icon">
              <i class="el-icon-bank-card"></i>
            </div>
            <div class="stat-info">
              <div class="stat-number">{{ statisticData.cardNum || 0 }}</div>
              <div class="stat-title">Total Member Cards</div>
            </div>
          </div>
        </el-card>
      </el-col>

      <el-col :xs="24" :sm="12" :md="6" :lg="6" :xl="6">
        <el-card class="stat-card">
          <div class="stat-content">
            <div class="stat-icon product-icon">
              <i class="el-icon-s-order"></i>
            </div>
            <div class="stat-info">
              <div class="stat-number">{{ statisticData.productCount || 0 }}</div>
              <div class="stat-title">Total Orders</div>
            </div>
          </div>
        </el-card>
      </el-col>

      <el-col :xs="24" :sm="12" :md="6" :lg="6" :xl="6">
        <el-card class="stat-card">
          <div class="stat-content">
            <div class="stat-icon revenue-icon">
              <i class="el-icon-money"></i>
            </div>
            <div class="stat-info">
              <div class="stat-number">{{ totalRevenue }}</div>
              <div class="stat-title">Total Revenue</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- transaction statistics section -->
    <el-row :gutter="20" class="deal-stats">
      <el-col :xs="24" :sm="8" :md="8" :lg="8" :xl="8">
        <el-card class="deal-card consume-card">
          <div class="deal-content">
            <div class="deal-icon">
              <i class="el-icon-shopping-cart-full"></i>
            </div>
            <div class="deal-info">
              <div class="deal-number">{{ consumeAmount }}</div>
              <div class="deal-title">Total Spending</div>
            </div>
          </div>
        </el-card>
      </el-col>

      <el-col :xs="24" :sm="8" :md="8" :lg="8" :xl="8">
        <el-card class="deal-card recharge-card">
          <div class="deal-content">
            <div class="deal-icon">
              <i class="el-icon-coin"></i>
            </div>
            <div class="deal-info">
              <div class="deal-number">{{ rechargeAmount }}</div>
              <div class="deal-title">Total Recharge</div>
            </div>
          </div>
        </el-card>
      </el-col>

      <el-col :xs="24" :sm="8" :md="8" :lg="8" :xl="8">
        <el-card class="deal-card integral-card">
          <div class="deal-content">
            <div class="deal-icon">
              <i class="el-icon-present"></i>
            </div>
            <div class="deal-info">
              <div class="deal-number">{{ integralAmount }}</div>
              <div class="deal-title">Points Redemption</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- charts section -->
    <el-row :gutter="20" class="chart-section">
      <el-col :xs="24" :sm="24" :md="24" :lg="24" :xl="24">
        <el-card class="chart-card">
          <div slot="header" class="chart-header">
            <div class="chart-title">
              <i class="el-icon-data-line" style="margin-right: 8px;"></i>
              Last Week Transaction Trends
            </div>
            <div class="chart-legend">
              <span class="legend-item">
                <span class="legend-color consume-color"></span>
                Spending
              </span>
              <span class="legend-item">
                <span class="legend-color recharge-color"></span>
                Recharge
              </span>
              <span class="legend-item">
                <span class="legend-color integral-color"></span>
                Points Redemption
              </span>
            </div>
          </div>
          <div id="trendChart" class="chart-container"></div>
        </el-card>
      </el-col>
    </el-row>

    <!-- data distribution section -->
    <el-row :gutter="20" class="distribution-section">
      <el-col :xs="24" :sm="12" :md="12" :lg="12" :xl="12">
        <el-card class="distribution-card">
          <div slot="header" class="card-header">
            <i class="el-icon-pie-chart" style="margin-right: 8px;"></i>
            Transaction Type Distribution
          </div>
          <div id="pieChart" class="chart-container-small"></div>
        </el-card>
      </el-col>

      <el-col :xs="24" :sm="12" :md="12" :lg="12" :xl="12">
        <el-card class="distribution-card">
          <div slot="header" class="card-header">
            <i class="el-icon-data-analysis" style="margin-right: 8px;"></i>
            Business Overview
          </div>
          <div class="business-overview">
            <div class="overview-item">
              <div class="overview-label">Member Card Ownership Rate</div>
              <div class="overview-value">{{ cardHolderRate }}%</div>
            </div>
            <div class="overview-item">
              <div class="overview-label">Recharge-to-Spend Ratio</div>
              <div class="overview-value">{{ rechargeConsumeRatio }}</div>
            </div>
            <div class="overview-item">
              <div class="overview-label">Points Usage Rate</div>
              <div class="overview-value">{{ integralUsageRate }}%</div>
            </div>
            <div class="overview-item">
              <div class="overview-label">Average Order Value</div>
              <div class="overview-value">{{ averageOrderValue }} CNY</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import { getStatisticData, getDealData, getChartData } from '@/api/dashboard'
import * as echarts from 'echarts'

export default {
  name: 'StatisticsOverview',
  data() {
    return {
      // stats data
      statisticData: {
        memberNum: 0,
        cardNum: 0,
        productCount: 0
      },

      // transaction data
      dealData: {
        consume: 0,
        recharge: 0,
        integral: 0
      },

      // chart data
      chartData: {
        consume: [0, 0, 0, 0, 0],
        recharge: [0, 0, 0, 0, 0],
        integral: [0, 0, 0, 0, 0]
      },

      loading: false,
      trendChart: null,
      pieChart: null
    }
  },
  computed: {
    ...mapGetters(['name']),

    // calculate total revenue
    totalRevenue() {
      const consume = Math.abs(this.dealData.consume || 0)
      const integral = Math.abs(this.dealData.integral || 0)
      return ((consume + integral) / 100).toFixed(2)
    },

    // total spending (in yuan)
    consumeAmount() {
      return (Math.abs(this.dealData.consume || 0) / 100).toFixed(2)
    },

    // total recharge (in yuan)
    rechargeAmount() {
      return ((this.dealData.recharge || 0) / 100).toFixed(2)
    },

    // points redemption total
    integralAmount() {
      return Math.abs(this.dealData.integral || 0)
    },

    // member card ownership rate
    cardHolderRate() {
      if (this.statisticData.memberNum === 0) return 0
      return ((this.statisticData.cardNum / this.statisticData.memberNum) * 100).toFixed(1)
    },

    // recharge-to-spend ratio
    rechargeConsumeRatio() {
      const consume = Math.abs(this.dealData.consume || 0)
      const recharge = this.dealData.recharge || 0
      if (consume === 0) return '∞'
      return (recharge / consume).toFixed(2)
    },

    // points usage rate
    integralUsageRate() {
      const totalPoints = this.statisticData.cardNum * 1000 // assuming 1000 pts per card on average
      const usedPoints = Math.abs(this.dealData.integral || 0)
      if (totalPoints === 0) return 0
      return ((usedPoints / totalPoints) * 100).toFixed(1)
    },

    // average order value
    averageOrderValue() {
      const consume = Math.abs(this.dealData.consume || 0)
      const orders = consume / 10000 // assuming avg 100 yuan per order, estimate order count
      if (orders === 0) return 0
      return ((consume / 100) / orders).toFixed(2)
    }
  },
  created() {
    this.fetchDashboardData()
  },
  mounted() {
    // ensure DOM is rendered before initializing charts
    this.$nextTick(() => {
      this.initCharts()
    })

    // listen for window resize
    window.addEventListener('resize', this.handleResize)
  },
  beforeDestroy() {
    // remove event listeners
    window.removeEventListener('resize', this.handleResize)
    // destroy chart instances
    if (this.trendChart) {
      this.trendChart.dispose()
    }
    if (this.pieChart) {
      this.pieChart.dispose()
    }
  },
  methods: {
    async fetchDashboardData() {
      this.loading = true
      try {
        // fetch all data in parallel
        const [statsResponse, dealResponse, chartResponse] = await Promise.all([
          getStatisticData(),
          getDealData(),
          getChartData()
        ])

        // process stats data
        if (statsResponse && statsResponse.data) {
          this.statisticData = {
            memberNum: statsResponse.data.memberNum || 0,
            cardNum: statsResponse.data.cardNum || 0,
            productCount: statsResponse.data.productCount || 0
          }
        }

        // process deal data
        if (dealResponse && dealResponse.data) {
          this.dealData = {
            consume: dealResponse.data.consume || 0,
            recharge: dealResponse.data.recharge || 0,
            integral: dealResponse.data.integral || 0
          }
        }

        // process chart data
        if (chartResponse && chartResponse.data) {
          this.chartData = {
            consume: chartResponse.data.consume || [0, 0, 0, 0, 0],
            recharge: chartResponse.data.recharge || [0, 0, 0, 0, 0],
            integral: chartResponse.data.integral || [0, 0, 0, 0, 0]
          }
        }

        // reinit charts after data update
        this.$nextTick(() => {
          this.initCharts()
        })

      } catch (error) {
        console.error('Failed to fetch dashboard data:', error)
        this.$message.error('Failed to fetch data, please try again later')
      } finally {
        this.loading = false
      }
    },

    initCharts() {
      this.initTrendChart()
      this.initPieChart()
    },

    initTrendChart() {
      const chartDom = document.getElementById('trendChart')
      if (!chartDom) return

      // destroy existing chart instance
      if (this.trendChart) {
        this.trendChart.dispose()
      }

      this.trendChart = echarts.init(chartDom)

      // get last week's dates (Mon-Fri)
      const dates = this.getLastWeekDates()

      const option = {
        title: {
          text: '',
          left: 'center'
        },
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'cross'
          },
          formatter: function(params) {
            let result = params[0].axisValue + '<br/>'
            params.forEach(item => {
              const value = Math.abs(item.value)
              if (item.seriesName === 'Points Redemption') {
                result += `${item.marker}${item.seriesName}: ${value} pts<br/>`
              } else {
                result += `${item.marker}${item.seriesName}: ¥${value}<br/>`
              }
            })
            return result
          }
        },
        legend: {
          data: ['Spending', 'Recharge', 'Points Redemption'],
          top: 10
        },
        grid: {
          left: '3%',
          right: '4%',
          bottom: '3%',
          containLabel: true
        },
        xAxis: {
          type: 'category',
          boundaryGap: false,
          data: dates,
          axisLabel: {
            formatter: '{value}'
          }
        },
        yAxis: [
          {
            type: 'value',
            name: 'Amount (CNY)',
            position: 'left',
            axisLabel: {
              formatter: '¥{value}'
            }
          },
          {
            type: 'value',
            name: 'Points',
            position: 'right',
            axisLabel: {
              formatter: '{value}'
            }
          }
        ],
        series: [
          {
            name: 'Spending',
            type: 'line',
            yAxisIndex: 0,
            data: this.chartData.consume.map(val => Math.abs(val) / 100),
            smooth: true,
            lineStyle: {
              color: '#F56C6C'
            },
            areaStyle: {
              color: {
                type: 'linear',
                x: 0,
                y: 0,
                x2: 0,
                y2: 1,
                colorStops: [{
                  offset: 0, color: 'rgba(245, 108, 108, 0.3)'
                }, {
                  offset: 1, color: 'rgba(245, 108, 108, 0.05)'
                }]
              }
            }
          },
          {
            name: 'Recharge',
            type: 'line',
            yAxisIndex: 0,
            data: this.chartData.recharge.map(val => Math.abs(val) / 100),
            smooth: true,
            lineStyle: {
              color: '#67C23A'
            },
            areaStyle: {
              color: {
                type: 'linear',
                x: 0,
                y: 0,
                x2: 0,
                y2: 1,
                colorStops: [{
                  offset: 0, color: 'rgba(103, 194, 58, 0.3)'
                }, {
                  offset: 1, color: 'rgba(103, 194, 58, 0.05)'
                }]
              }
            }
          },
          {
            name: 'Points Redemption',
            type: 'line',
            yAxisIndex: 1,
            data: this.chartData.integral.map(val => Math.abs(val)),
            smooth: true,
            lineStyle: {
              color: '#E6A23C'
            },
            areaStyle: {
              color: {
                type: 'linear',
                x: 0,
                y: 0,
                x2: 0,
                y2: 1,
                colorStops: [{
                  offset: 0, color: 'rgba(230, 162, 60, 0.3)'
                }, {
                  offset: 1, color: 'rgba(230, 162, 60, 0.05)'
                }]
              }
            }
          }
        ]
      }

      this.trendChart.setOption(option)
    },

    initPieChart() {
      const chartDom = document.getElementById('pieChart')
      if (!chartDom) return

      // destroy existing chart instance
      if (this.pieChart) {
        this.pieChart.dispose()
      }

      this.pieChart = echarts.init(chartDom)

      const consume = Math.abs(this.dealData.consume || 0) / 100
      const recharge = (this.dealData.recharge || 0) / 100
      const integral = Math.abs(this.dealData.integral || 0)

      const option = {
        tooltip: {
          trigger: 'item',
          formatter: '{a} <br/>{b}: ¥{c} ({d}%)'
        },
        legend: {
          orient: 'horizontal',
          bottom: 10,
          data: ['Spending', 'Recharge', 'Points Redemption']
        },
        series: [
          {
            name: 'Transaction Amount',
            type: 'pie',
            radius: ['40%', '70%'],
            center: ['50%', '45%'],
            avoidLabelOverlap: false,
            label: {
              show: true,
              formatter: '{b}\n¥{c}'
            },
            emphasis: {
              label: {
                show: true,
                fontSize: '16',
                fontWeight: 'bold'
              }
            },
            data: [
              {
                value: consume,
                name: 'Spending',
                itemStyle: { color: '#F56C6C' }
              },
              {
                value: recharge,
                name: 'Recharge',
                itemStyle: { color: '#67C23A' }
              },
              {
                value: integral,
                name: 'Points Redemption',
                itemStyle: { color: '#E6A23C' }
              }
            ]
          }
        ]
      }

      this.pieChart.setOption(option)
    },

    getLastWeekDates() {
      const dates = []
      const today = new Date()
      const lastWeek = new Date(today.getTime() - 7 * 24 * 60 * 60 * 1000)

      // get last week's Monday
      const monday = new Date(lastWeek)
      monday.setDate(lastWeek.getDate() - lastWeek.getDay() + 1)

      // generate dates for Mon-Fri
      for (let i = 0; i < 5; i++) {
        const date = new Date(monday)
        date.setDate(monday.getDate() + i)
        dates.push(`${date.getMonth() + 1}-${date.getDate()}`)
      }

      return dates
    },

    handleResize() {
      if (this.trendChart) {
        this.trendChart.resize()
      }
      if (this.pieChart) {
        this.pieChart.resize()
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.dashboard-container {
  padding: 20px;
  background-color: #f5f7fa;
  min-height: calc(100vh - 50px);
}

// stats card styles
.stats-cards {
  margin-bottom: 20px;
}

.stat-card {
  height: 120px;
  cursor: pointer;
  transition: all 0.3s ease;

  &:hover {
    transform: translateY(-5px);
    box-shadow: 0 8px 25px rgba(0, 0, 0, 0.1);
  }

  .stat-content {
    display: flex;
    align-items: center;
    height: 100%;

    .stat-icon {
      width: 60px;
      height: 60px;
      border-radius: 50%;
      display: flex;
      align-items: center;
      justify-content: center;
      margin-right: 20px;

      i {
        font-size: 24px;
        color: white;
      }
    }

    .member-icon {
      background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
    }

    .card-icon {
      background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
    }

    .product-icon {
      background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
    }

    .revenue-icon {
      background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
    }

    .stat-info {
      flex: 1;

      .stat-number {
        font-size: 32px;
        font-weight: bold;
        color: #2c3e50;
        line-height: 1;
        margin-bottom: 8px;
      }

      .stat-title {
        font-size: 14px;
        color: #7f8c8d;
      }
    }
  }
}

// deal stats styles
.deal-stats {
  margin-bottom: 20px;
}

.deal-card {
  height: 100px;
  cursor: pointer;
  transition: all 0.3s ease;

  &:hover {
    transform: translateY(-3px);
  }

  .deal-content {
    display: flex;
    align-items: center;
    height: 100%;

    .deal-icon {
      width: 50px;
      height: 50px;
      border-radius: 10px;
      display: flex;
      align-items: center;
      justify-content: center;
      margin-right: 15px;

      i {
        font-size: 20px;
        color: white;
      }
    }

    .deal-info {
      flex: 1;

      .deal-number {
        font-size: 24px;
        font-weight: bold;
        color: white;
        line-height: 1;
        margin-bottom: 5px;
      }

      .deal-title {
        font-size: 12px;
        color: rgba(255, 255, 255, 0.8);
      }
    }
  }

  &.consume-card {
    background: linear-gradient(135deg, #F56C6C 0%, #ff6b6b 100%);

    .deal-icon {
      background: rgba(255, 255, 255, 0.2);
    }
  }

  &.recharge-card {
    background: linear-gradient(135deg, #67C23A 0%, #85ce61 100%);

    .deal-icon {
      background: rgba(255, 255, 255, 0.2);
    }
  }

  &.integral-card {
    background: linear-gradient(135deg, #E6A23C 0%, #ebb563 100%);

    .deal-icon {
      background: rgba(255, 255, 255, 0.2);
    }
  }
}

// chart section styles
.chart-section {
  margin-bottom: 20px;
}

.chart-card {
  .chart-header {
    display: flex;
    justify-content: space-between;
    align-items: center;

    .chart-title {
      font-size: 16px;
      font-weight: bold;
      color: #2c3e50;
    }

    .chart-legend {
      .legend-item {
        margin-left: 20px;
        font-size: 12px;
        color: #7f8c8d;

        .legend-color {
          display: inline-block;
          width: 12px;
          height: 12px;
          border-radius: 2px;
          margin-right: 5px;

          &.consume-color {
            background-color: #F56C6C;
          }

          &.recharge-color {
            background-color: #67C23A;
          }

          &.integral-color {
            background-color: #E6A23C;
          }
        }
      }
    }
  }

  .chart-container {
    height: 400px;
    width: 100%;
  }
}

// data distribution section styles
.distribution-section {
  margin-bottom: 20px;
}

.distribution-card {
  .card-header {
    font-size: 16px;
    font-weight: bold;
    color: #2c3e50;
  }

  .chart-container-small {
    height: 300px;
    width: 100%;
  }
}

.business-overview {
  .overview-item {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 15px 0;
    border-bottom: 1px solid #ecf0f1;

    &:last-child {
      border-bottom: none;
    }

    .overview-label {
      font-size: 14px;
      color: #7f8c8d;
    }

    .overview-value {
      font-size: 18px;
      font-weight: bold;
      color: #2c3e50;
    }
  }
}

// responsive design
@media (max-width: 768px) {
  .dashboard-container {
    padding: 10px;
  }

  .stat-card {
    margin-bottom: 15px;

    .stat-content {
      .stat-icon {
        width: 50px;
        height: 50px;
        margin-right: 15px;

        i {
          font-size: 20px;
        }
      }

      .stat-info {
        .stat-number {
          font-size: 24px;
        }
      }
    }
  }

  .deal-card {
    margin-bottom: 15px;
  }

  .chart-container {
    height: 300px;
  }

  .chart-container-small {
    height: 250px;
  }
}
</style>