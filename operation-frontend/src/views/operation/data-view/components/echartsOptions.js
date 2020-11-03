export default {
    fansAdd: {
      color: ['#fbda68', '#70d391', '#61b1ff', '#ffb980'],
      tooltip: {
        trigger: 'axis',
        axisPointer: {
          lineStyle: {
            width: 0.5
          }
        }
      },
      legend: {
        data: ['净增长', '取消关注', '新增关注']
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
        data: []
      },
      yAxis: {
        type: 'value',
        splitLine: {
          lineStyle: {
            type: 'dashed',
            color: ['#cccccc'],
            width: 0.5
          }
        }
      },
      series: [
        {
          lineStyle: {
            width: 1
          },
          name: '净增长',
          type: 'line',
          smooth: true,
          data: []
        },
        {
          lineStyle: {
            width: 1
          },
          name: '取消关注',
          type: 'line',
          smooth: true,
          data: []
        },
        {
          lineStyle: {
            width: 1
          },
          name: '新增关注',
          type: 'line',
          smooth: true,
          data: []
        }
      ]
    },
    fansPro: {
      color: ['#61b1ff', '#70d391', '#fbda68', '#ffb980', '#62d3d4', '#feba4a'],
      tooltip: {
        trigger: 'item',
        formatter: '{b} ({d}%)'
      },
      series: [{
        type: 'pie',
        radius: ['50%', '70%'],
        data: [],
        emphasis: {
          label: {
            show: true,
            fontSize: '20',
            fontWeight: 'bold'
          }
        },
        avoidLabelOverlap: false,
        label: {
          position: 'outer',
          alignTo: 'none',
          bleedMargin: 5
        }
      }]
    },
    city: {
      visualMap: {
        min: 0,
        max: 34,
        left: 'left',
        top: 'bottom',
        text: ['高', '低'], // 取值范围的文字
        inRange: {
          color: ['#e0ffff', '#006edd']// 取值范围的颜色
        },
        realtime: true,
        show: true// 图注
      },
      geo: {
        map: 'china',
        roam: false, // 不开启缩放和平移
        zoom: 1.23, // 视角缩放比例
        label: {
          normal: {
            show: true,
            fontSize: '10',
            color: 'rgba(0,0,0,0.7)'
          }
        },
        itemStyle: {
          normal: {
            borderColor: 'rgba(0, 0, 0, 0.2)'
          },
          emphasis: {
            areaColor: '#F3B329', // 鼠标选择区域颜色
            shadowOffsetX: 0,
            shadowOffsetY: 0,
            shadowBlur: 20,
            borderWidth: 0,
            shadowColor: 'rgba(0, 0, 0, 0.5)'
          }
        }
      },
      series: [
        {
          type: 'map',
          geoIndex: 0,
          data: []
        }
      ]
    },
    fansActive: {
      color: ['#fbda68', '#70d391', '#61b1ff', '#ffb980'],
      tooltip: {
        trigger: 'axis',
        axisPointer: {
          type: 'cross',
          label: {
            backgroundColor: '#6a7985'
          }
        }
      },
      legend: {
        data: ['48小时内容互动', '48小时 - 7天互动', '7小时 - 15天互动']
      },
      grid: {
        left: '3%',
        right: '4%',
        bottom: '3%',
        containLabel: true
      },
      xAxis: [
        {
          type: 'category',
          boundaryGap: false,
          data: []
        }
      ],
      yAxis: [
        {
          type: 'value'
        }
      ],
      series: [
        {
          name: '48小时内容互动',
          type: 'line',
          areaStyle: {},
          data: []
        },
        {
          name: '48小时 - 7天互动',
          type: 'line',
          areaStyle: {},
          data: []
        },
        {
          name: '7小时 - 15天互动',
          type: 'line',
          areaStyle: {},
          data: []
        }
      ]
    },
    imgText: {
      color: ['#fbda68', '#70d391', '#61b1ff', '#ffb980'],
      tooltip: {
        trigger: 'axis',
        axisPointer: {
          lineStyle: {
            width: 0.5
          }
        }
      },
      legend: {
        data: ['阅读人数', '阅读次数', '分享人数', '分享次数', '阅读原文人数', '阅读原文次数']
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
        data: []
      },
      yAxis: {
        type: 'value',
        splitLine: {
          lineStyle: {
            type: 'dashed',
            color: ['#cccccc'],
            width: 0.5
          }
        }
      },
      series: [
        {
          lineStyle: {
            width: 1
          },
          name: '阅读人数',
          type: 'line',
          smooth: true,
          data: []
        },
        {
          lineStyle: {
            width: 1
          },
          name: '阅读次数',
          type: 'line',
          smooth: true,
          data: []
        },
        {
          lineStyle: {
            width: 1
          },
          name: '分享人数',
          type: 'line',
          smooth: true,
          data: []
        },
        {
          lineStyle: {
            width: 1
          },
          name: '分享次数',
          type: 'line',
          smooth: true,
          data: []
        },
        {
          lineStyle: {
            width: 1
          },
          name: '阅读原文人数',
          type: 'line',
          smooth: true,
          data: []
        },
        {
          lineStyle: {
            width: 1
          },
          name: '阅读原文次数',
          type: 'line',
          smooth: true,
          data: []
        }
      ]
    },
    toNews: {
      color: ['#ffb04d', '#59cdcd', '#fcd468', '#69cc8a', '#52a9fb'],
      tooltip: {
        trigger: 'axis',
        axisPointer: {
          type: 'cross',
          label: {
            backgroundColor: '#6a7985'
          }
        }
      },
      legend: {
        data: ['粉丝消息', '关注', '取关', '扫描二维码', '菜单点击']
      },
      grid: {
        left: '3%',
        right: '4%',
        bottom: '3%',
        containLabel: true
      },
      xAxis: [
        {
          type: 'category',
          boundaryGap: false,
          data: []
        }
      ],
      yAxis: [
        {
          type: 'value'
        }
      ],
      series: [
        {
          name: '粉丝消息',
          type: 'line',
          areaStyle: {},
          data: []
        },
        {
          name: '关注',
          type: 'line',
          areaStyle: {},
          data: []
        },
        {
          name: '取关',
          type: 'line',
          areaStyle: {},
          data: []
        },
        {
          name: '扫描二维码',
          type: 'line',
          areaStyle: {},
          data: []
        },
        {
          name: '菜单点击',
          type: 'line',
          areaStyle: {},
          data: []
        }
      ]
    }
  }
