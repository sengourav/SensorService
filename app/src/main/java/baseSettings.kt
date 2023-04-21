import android.graphics.Color
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.sensorservice.MainActivity
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet

open class baseSettings:AppCompatActivity(){
    lateinit var mChart:LineChart
    var thread: Thread?=null
    var plotData:Boolean=true
    protected fun commonSettings(SensorType:Int){
        mChart.description.isEnabled=true
        mChart.isDragEnabled=true//we can drag data in the plot
        mChart.setScaleEnabled(false)//dont know
        mChart.setDrawGridBackground(false) //greye the bacckground
        mChart.setPinchZoom(false) //dont know its functionality
        mChart.setBackgroundColor(Color.WHITE)
        mChart.setTouchEnabled(true)//dont know
        val data = LineData()
        data.setValueTextColor(Color.BLACK)
        mChart.data = data
        // get the legend (only possible after setting data)
        // get the legend (only possible after setting data)
        val l = mChart.legend
        // modify the legend ...
        // modify the legend ...
        l.form = Legend.LegendForm.LINE
        l.textColor = Color.BLACK

        val xl = mChart.xAxis
        xl.textColor = Color.BLACK
        xl.setDrawGridLines(true)
        xl.setAvoidFirstLastClipping(true)
        xl.isEnabled = true

        // Get the data object for the chart
        val leftAxis = mChart.axisLeft
        leftAxis.textColor = Color.BLACK
        leftAxis.setDrawGridLines(true)
        if(SensorType== Sensor.TYPE_GYROSCOPE){
            leftAxis.axisMaximum =0.2f
            leftAxis.axisMinimum =-0.2f
        }
        else if (SensorType== Sensor.TYPE_ACCELEROMETER){
            leftAxis.axisMaximum =39f
            leftAxis.axisMinimum =-39f
        }
        else if (SensorType== Sensor.TYPE_MAGNETIC_FIELD){
            leftAxis.axisMaximum =100f
            leftAxis.axisMinimum =-100f
        }
        else{
            leftAxis.axisMaximum =500f
            leftAxis.axisMinimum =0f
        }
        val rightAxis = mChart.axisRight
        rightAxis.isEnabled = false

        mChart.axisLeft.setDrawGridLines(true)
        mChart.xAxis.setDrawGridLines(true)
        mChart.setDrawBorders(true)
    }

    protected fun addEntry(event: SensorEvent) {
        var data: LineData =mChart.data
        var k:Char
        if(event?.sensor?.type==Sensor.TYPE_LIGHT){
            if(data!=null){
                var setl: ILineDataSet? =data.getDataSetByIndex(0)
                if (setl==null){
                    k='x'
                    setl=createDataSet(k)
                    setl?.label= "lx".toString()
                    data.addDataSet(setl)
                }
                data.addEntry(Entry(setl!!.entryCount.toFloat(),event.values[0]),0)
                mChart.notifyDataSetChanged()
                mChart.setVisibleXRangeMaximum(70F)
                mChart.moveViewToX(data.entryCount.toFloat())
            }
        }
        else{
            if(data!=null){
                var setx: ILineDataSet? =data.getDataSetByIndex(0)
                var sety: ILineDataSet? =data.getDataSetByIndex(1)
                var setz: ILineDataSet? =data.getDataSetByIndex(2)
                if (setx==null){
                    k='x'
                    setx=createDataSet(k)
                    setx?.label='x'.toString()
                    data.addDataSet(setx)
                }
                if (sety==null){
                    k='y'
                    sety=createDataSet(k)
                    sety?.label='y'.toString()
                    data.addDataSet(sety)
                }
                if (setz==null){
                    k='z'
                    setz=createDataSet(k)
                    setz?.label='z'.toString()
                    data.addDataSet(setz)
                }
                data.addEntry(Entry(setx!!.entryCount.toFloat(),event.values[0]),0)
                data.addEntry(Entry(sety!!.entryCount.toFloat(),event.values[1]),1)
                data.addEntry(Entry(setz!!.entryCount.toFloat(),event.values[2]),2)
                data.notifyDataChanged()

// Set the y-axis limits to include the data

// Set the y-axis limits to include the data
//            mChart.axisLeft.axisMaximum=data.yMax
//            mChart.axisLeft.axisMinimum= data.yMin
            mChart.notifyDataSetChanged()
            mChart.setVisibleXRangeMaximum(70F)
            mChart.moveViewToX(data.entryCount.toFloat())
                //  data.remo

            }
        }

    }

    protected fun createDataSet(p: Char): ILineDataSet? {
        var set: LineDataSet = LineDataSet(null,"Dynamic Data")
        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        set.setLineWidth(3f);
        set.setHighlightEnabled(false);
        set.setDrawValues(false);
        set.setDrawCircles(false);
        set.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        set.setCubicIntensity(0.2f);
        if(p=='x'){
            set.setColor(Color.RED);
        }
        else if (p=='y'){
            set.setColor(Color.GREEN)
        }
        else{
            set.setColor(Color.BLUE)
        }
        return set;
    }

    protected fun startPlot() {
        thread?.interrupt()

        thread = Thread {
            while (true) {
                plotData = true
                try {
                    Thread.sleep(10)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            }
        }

        thread?.start()

    }

}