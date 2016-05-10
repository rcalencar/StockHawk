package com.sam_chordas.android.stockhawk.ui;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sam_chordas.android.stockhawk.R;
import com.sam_chordas.android.stockhawk.data.QuoteColumns;
import com.sam_chordas.android.stockhawk.data.QuoteProvider;
import com.sam_chordas.android.stockhawk.rest.DetailCursorRecyclerViewAdapter;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.Unbinder;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.LineChartView;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int CURSOR_LOADER_ID = 8001;
    private static final String TAG = "DetailFragment";
    public static final int MINIMUM_TO_DRAW = 4;

    private Unbinder unbinder;
    private String mSymbol;
    private LineChartView chart;
    private int numberOfPoints;
    private LineChartData data;
    private Cursor mCursor;

    private boolean hasAxes = true;
    private boolean hasAxesNames = true;
    private boolean hasLines = true;
    private boolean hasPoints = true;
    private ValueShape shape = ValueShape.CIRCLE;
    private boolean isFilled = false;
    private boolean hasLabels = false;
    private boolean isCubic = false;
    private boolean hasLabelForSelected = false;
    private boolean pointsHaveDifferentColor;
    private RecyclerView recyclerView;
    private DetailCursorRecyclerViewAdapter mCursorAdapter;
    private View noChart;

    public DetailFragment() {
    }

    public static DetailFragment newInstance(String symbol) {
        DetailFragment fragment = new DetailFragment();
        Bundle args = new Bundle();
        args.putString("symbol", symbol);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mSymbol = getArguments().getString("symbol");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        getLoaderManager().initLoader(CURSOR_LOADER_ID, null, this);

        noChart = rootView.findViewById(R.id.detail_no_chart);

        chart = (LineChartView) rootView.findViewById(R.id.detail_chart);
//        chart.setViewportCalculationEnabled(false);
//        resetViewport();

        mCursorAdapter = new DetailCursorRecyclerViewAdapter(getContext(), null);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.detail_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(mCursorAdapter);

        return rootView;
    }

    private void resetViewport() {
        // Reset viewport height range to (0,100)
        final Viewport v = new Viewport(chart.getMaximumViewport());
        v.bottom = 0;
        v.top = 100;
        v.left = 0;
        v.right = numberOfPoints - 1;
        chart.setMaximumViewport(v);
        chart.setCurrentViewport(v);
    }

    private void generateChart() {
        List<PointValue> yValues = new ArrayList<PointValue>();
        List<AxisValue> axisValues = new ArrayList<AxisValue>();

        int countX = 0;
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        try {
            while (mCursor.moveToNext()) {
                float y = decimalFormat.parse(mCursor.getString(mCursor.getColumnIndex(QuoteColumns.BIDPRICE))).floatValue();
                long x = mCursor.getLong(mCursor.getColumnIndex(QuoteColumns.CREATED));

                Log.d(TAG, "x " + x + ", y " + y);
                yValues.add(new PointValue(++countX, y));
                AxisValue axisValue = new AxisValue(x);
                axisValues.add(axisValue);
            }
        } catch (Exception ex) {
            Log.d(DetailFragment.class.getSimpleName(), ex.getMessage(), ex);
        }

        List<Line> lines = new ArrayList<>();
        Line line = new Line(yValues);
        line.setColor(ChartUtils.COLORS[0]);
        line.setShape(shape);
        line.setCubic(isCubic);
        line.setFilled(isFilled);
//        line.setHasLabels(false);
        line.setHasLabelsOnlyForSelected(hasLabelForSelected);
        line.setHasLines(hasLines);
        line.setHasPoints(false);
        lines.add(line);

        data = new LineChartData(lines);
        data.setAxisXBottom(new Axis(axisValues).setHasSeparationLine(false));
//        data.setAxisXBottom(null);
        data.setAxisYLeft(null);
        data.setBaseValue(Float.NEGATIVE_INFINITY);

        chart.setLineChartData(data);
//        resetViewport();

        Log.d(DetailFragment.class.getSimpleName(), "generated data");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
//        unbinder.unbind();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        // This narrows the return to only the stocks that are most current.
        return new CursorLoader(getContext(), QuoteProvider.Quotes.CONTENT_URI,
                new String[]{QuoteColumns._ID, QuoteColumns.SYMBOL, QuoteColumns.BIDPRICE,
                        QuoteColumns.PERCENT_CHANGE, QuoteColumns.CHANGE, QuoteColumns.ISUP, QuoteColumns.CREATED},
                QuoteColumns.SYMBOL + " = ?",
                new String[]{mSymbol},
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mCursor = data;

        if(data.getCount() >= MINIMUM_TO_DRAW) {
            chart.setVisibility(View.VISIBLE);
            noChart.setVisibility(View.GONE);
            generateChart();
        } else {
            chart.setVisibility(View.GONE);
            noChart.setVisibility(View.VISIBLE);
        }

        data.moveToPosition(-1);
        mCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mCursorAdapter.swapCursor(null);
    }
}
