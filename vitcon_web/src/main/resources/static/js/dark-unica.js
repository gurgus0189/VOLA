/*
 Highcharts JS v7.2.0 (2019-09-03)

 (c) 2009-2019 Torstein Honsi

 License: www.highcharts.com/license
*/
(function(a) {
  "object" === typeof module && module.exports ? (a["default"] = a, module.exports = a) : "function" === typeof define && define.amd ? define("highcharts/themes/dark-unica", ["highcharts"], function(b) {
    a(b);
    a.Highcharts = b;
    return a
  }) : a("undefined" !== typeof Highcharts ? Highcharts : void 0)
})(function(a) {
  function b(a, b, c, d) {
    a.hasOwnProperty(b) || (a[b] = d.apply(null, c))
  }
  a = a ? a._modules : {};
  b(a, "themes/dark-unica.js", [a["parts/Globals.js"]], function(a) {
    a.createElement("link", {
      href: "https://fonts.googleapis.com/css?family=Unica+One",
      rel: "stylesheet",
      type: "text/css"
    }, null, document.getElementsByTagName("head")[0]);
    a.theme = {
      colors: "#fff #90ee7e #f45b5b #7798BF #aaeeee #ff0066 #eeaaee #55BF3B #DF5353 #7798BF #aaeeee".split(" "),
      chart: {
        backgroundColor: {
          linearGradient: {
            x1: 0,
            y1: 0,
            x2: 1,
            y2: 1
          },
          stops: [
            [0, "#122835"],
            [1, "#122835"]
          ]
        },
        style: {
          fontFamily: "'QuestrialRegular', sans-serif"
        },
        plotBorderColor: "#303539"
      },
      title: {
        style: {
          color: "#fff",
          textTransform: "uppercase",
          fontSize: "20px"
        }
      },
      subtitle: {
        style: {
          color: "#fff",
          textTransform: "uppercase"
        }
      },
      xAxis: {
        gridLineColor: "#384953",
        labels: {
            style: {
                color: '#fff',
                fontSize: '16px'
            }
        },
        lineColor: "#384953",
        minorGridLineColor: "#0c3865",
        tickColor: "#384953",
        title: {
          style: {
            color: "#A0A0A3"
          }
        }
      },
      yAxis: {
        gridLineColor: "#384953",
        labels: {
          style: {
            color: "#fff",
            fontSize: '15px'
          }
        },
        lineColor: "#384953",
        minorGridLineColor: "#0c3865",
        tickColor: "#384953",
        tickWidth: 1,
        title: {
          style: {
            color: "#fff"
          }
        }
      },
      tooltip: {
        backgroundColor: "rgba(255, 255, 255)",
        style: {
          color: "#000",
          fontSize: "14px"
        }
      },
      plotOptions: {
        series: {
          dataLabels: {
            color: "#F0F0F3",
            style: {
              fontSize: "13px"
            }
          },
          marker: {
            lineColor: "#333"
          }
        },
        boxplot: {
          fillColor: "#0c3865"
        },
        candlestick: {
          lineColor: "white"
        },
        errorbar: {
          color: "white"
        }
      },
      legend: {
	    layout: 'horizontal',
        align: 'center',
        verticalAlign: 'bottom',
    	backgroundColor: '#122835',
        itemStyle: {
          color: "#fff",
          fontSize: '1em',
          fontStyle: 'normal',
          fontWeight: 'bold',
        },
        itemHoverStyle: {
          color: "#FFF"
        },
        itemHiddenStyle: {
          color: "#5D5D5D"
        },
        title: {
          style: {
            color: "#C0C0C0"
          }
        }
      },
      credits: {
        style: {
          color: "#666"
        }
      },
      labels: {
        style: {
          color: "#384953"
        }
      },
      drilldown: {
        activeAxisLabelStyle: {
          color: "#F0F0F3"
        },
        activeDataLabelStyle: {
          color: "#F0F0F3"
        }
      },
      navigation: {
        buttonOptions: {
          symbolStroke: "#DDDDDD",
          theme: {
            fill: "#626063",
            style: {
                color: '#fff',
                fontSize: '1em'
            },
            states: {
                hover: {
                    fill: '#fff',
                    style: {
                    	color: '#000'
                    }
                },
                select: {
                    fill: '#329dd8',
                    style: {
                    	color: '#fff'
                    }
                }
            }
          }
        }
      },  
      rangeSelector: {
        buttonTheme: {
          width: 43,
          fill: "#193C5C",
          stroke: "#fff",
          style: {
            color: "#fff",
            fontSize: '1em',
          },
          states: {
            hover: {
              fill: "#fff",
              stroke: "#fff",
              style: {
                color: "#000"
              }
            },
            select: {
              fill: "#329dd8",
              stroke: "#fff",
              style: {
                color: "white"
              }
            }
          }
        },
        inputBoxBorderColor: "#0c3865",
        inputStyle: {
          backgroundColor: "#fff",
          color: "#fff"
        },
        labelStyle: {
          color: "#fff"
        }
      },
      navigator: {
        handles: {
          backgroundColor: "#959593",
          borderColor: "#000",
          lineWidth: 1,
          width: 30,
          height: 30
        },
        outlineColor: "#404B51",
        maskFill: "rgba(255,255,255,0.1)",
        series: {
          color: "#7798BF",
          lineColor: "#A6C7ED"
        },
        xAxis: {
          gridLineColor: "#0c3865"
        }
      },
      scrollbar: {
        barBackgroundColor: "#303539",
        barBorderColor: "#303539",
        buttonArrowColor: "#ACACAC",
        buttonBackgroundColor: "#303539",
        buttonBorderColor: "#303539",
        rifleColor: "#FFF",
        trackBackgroundColor: "#404043",
        trackBorderColor: "#404043"
      }
    };
    a.setOptions(a.theme)
  });
  b(a, "masters/themes/dark-unica.src.js", [], function() {})
});
//# sourceMappingURL=dark-unica.js.map