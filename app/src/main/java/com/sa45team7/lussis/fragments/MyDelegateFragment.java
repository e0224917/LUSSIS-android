package com.sa45team7.lussis.fragments;


import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.sa45team7.lussis.R;
import com.sa45team7.lussis.data.UserManager;
import com.sa45team7.lussis.rest.LUSSISClient;
import com.sa45team7.lussis.rest.model.Delegate;
import com.sa45team7.lussis.rest.model.Employee;
import com.sa45team7.lussis.rest.model.LUSSISResponse;
import com.sa45team7.lussis.utils.DateConvertUtil;
import com.sa45team7.lussis.utils.ErrorUtil;

import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyDelegateFragment extends Fragment {

    private String deptCode;
    private SwipeRefreshLayout refreshLayout;
    private AutoCompleteTextView empNameView;
    private EditText startDateView;
    private EditText endDateView;
    private Employee chosenEmployee;
    private Calendar startCalendar = Calendar.getInstance();
    private Calendar endCalendar = Calendar.getInstance();
    private Button assignButton;
    private LinearLayout buttonsLayout;

    private Delegate currentDelegate;

    public MyDelegateFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        deptCode = UserManager.getInstance().getCurrentEmployee().getDeptCode();

        View view = inflater.inflate(R.layout.fragment_my_delegate, container, false);

        refreshLayout = view.findViewById(R.id.delegate_refresh_layout);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getDelegate();
            }
        });

        empNameView = view.findViewById(R.id.employee_name_auto_complete);
        fetchEmployeeList();

        startDateView = view.findViewById(R.id.start_date_text);
        endDateView = view.findViewById(R.id.end_date_text);
        setUpDatePicker(startDateView, startCalendar);
        setUpDatePicker(endDateView, endCalendar);

        assignButton = view.findViewById(R.id.assign_new_button);
        assignButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                assignNewDelegate();
            }
        });

        buttonsLayout = view.findViewById(R.id.buttons_layout);

        Button revokeButton = view.findViewById(R.id.revoke_button);
        revokeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                revokeDelegate();
            }
        });

        Button updateButton = view.findViewById(R.id.update_button);
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateDelegate();
            }
        });

        getDelegate();
        return view;
    }

    private void setUpDatePicker(final EditText dateView, final Calendar calendar) {
        final DatePickerDialog.OnDateSetListener dateListener = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                dateView.setText(DateConvertUtil.convertForDetail(calendar.getTime()));
            }

        };

        dateView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getContext(), dateListener, calendar
                        .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    private void getDelegate() {
        Call<Delegate> call = LUSSISClient.getApiService().getDelegate(deptCode);

        call.enqueue(new Callback<Delegate>() {
            @Override
            public void onResponse(Call<Delegate> call, Response<Delegate> response) {
                if (response.isSuccessful() && response.body() != null) {
                    currentDelegate = response.body();
                    empNameView.setText(currentDelegate.getEmployee().getFullName());

                    String startDate = DateConvertUtil.convertForDetail(currentDelegate.getStartDate());
                    startDateView.setText(startDate);

                    String endDate = DateConvertUtil.convertForDetail(currentDelegate.getEndDate());
                    startDateView.setText(endDate);

                } else {
                    currentDelegate = null;
                    String error = ErrorUtil.parseError(response).getMessage();
                    Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
                }
                showButtons();
                refreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<Delegate> call, Throwable t) {
                Toast.makeText(getContext(),
                        "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                refreshLayout.setRefreshing(false);
            }
        });
    }

    private void fetchEmployeeList() {
        Call<List<Employee>> call = LUSSISClient.getApiService()
                .getEmployeeListForDelegate(deptCode);

        call.enqueue(new Callback<List<Employee>>() {
            @Override
            public void onResponse(Call<List<Employee>> call, Response<List<Employee>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Employee> list = response.body();

                    ArrayAdapter<Employee> adapter = new ArrayAdapter<Employee>(getContext(),
                            android.R.layout.simple_dropdown_item_1line, list);

                    empNameView.setAdapter(adapter);

                    empNameView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            chosenEmployee = (Employee) parent.getItemAtPosition(position);
//                            empNameView.setText(chosenEmployee.getFullName());
                        }
                    });

                } else {
                    String error = ErrorUtil.parseError(response).getMessage();
                    Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Employee>> call, Throwable t) {
                Toast.makeText(getContext(),
                        "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                refreshLayout.setRefreshing(false);
            }
        });
    }

    private void assignNewDelegate() {
        updateCurrentDelegate();

        Call<Delegate> call = LUSSISClient.getApiService().postDelegate(deptCode, currentDelegate);
        call.enqueue(new Callback<Delegate>() {
            @Override
            public void onResponse(Call<Delegate> call, Response<Delegate> response) {
                if (response.isSuccessful() && response.body() != null) {
                    currentDelegate = response.body();
                    Toast.makeText(getContext(), "Added new delegate", Toast.LENGTH_SHORT).show();
                } else {
                    String error = ErrorUtil.parseError(response).getMessage();
                    Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
                }

                showButtons();
            }

            @Override
            public void onFailure(Call<Delegate> call, Throwable t) {
                Toast.makeText(getContext(),
                        "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                refreshLayout.setRefreshing(false);
            }
        });
    }

    private void updateDelegate() {
        updateCurrentDelegate();

        Call<LUSSISResponse> call = LUSSISClient.getApiService().updateDelegate(deptCode, currentDelegate);
        call.enqueue(new Callback<LUSSISResponse>() {
            @Override
            public void onResponse(Call<LUSSISResponse> call, Response<LUSSISResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();

                } else {
                    String error = ErrorUtil.parseError(response).getMessage();
                    Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
                }

                showButtons();
            }

            @Override
            public void onFailure(Call<LUSSISResponse> call, Throwable t) {
                Toast.makeText(getContext(),
                        "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                refreshLayout.setRefreshing(false);
            }
        });
    }

    private void revokeDelegate() {
        Call<LUSSISResponse> call = LUSSISClient.getApiService().deleteDelegate(deptCode);
        call.enqueue(new Callback<LUSSISResponse>() {
            @Override
            public void onResponse(Call<LUSSISResponse> call, Response<LUSSISResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    currentDelegate = null;

                } else {
                    String error = ErrorUtil.parseError(response).getMessage();
                    Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
                }
                showButtons();
            }

            @Override
            public void onFailure(Call<LUSSISResponse> call, Throwable t) {
                Toast.makeText(getContext(),
                        "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                refreshLayout.setRefreshing(false);
            }
        });
    }

    private void updateCurrentDelegate() {
        if(currentDelegate == null) currentDelegate = new Delegate();
        currentDelegate.setEmployee(chosenEmployee);
        currentDelegate.setStartDate(startCalendar.getTime());
        currentDelegate.setEndDate(endCalendar.getTime());
    }

    private void showButtons() {
        buttonsLayout.setVisibility(currentDelegate == null ? View.GONE : View.VISIBLE);
    }
}
