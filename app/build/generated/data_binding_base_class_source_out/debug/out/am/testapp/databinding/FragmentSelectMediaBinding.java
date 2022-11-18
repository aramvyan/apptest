// Generated by view binder compiler. Do not edit!
package am.testapp.databinding;

import am.testapp.R;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class FragmentSelectMediaBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final ProgressBar progressLayout;

  @NonNull
  public final Button selectMediaButton;

  private FragmentSelectMediaBinding(@NonNull ConstraintLayout rootView,
      @NonNull ProgressBar progressLayout, @NonNull Button selectMediaButton) {
    this.rootView = rootView;
    this.progressLayout = progressLayout;
    this.selectMediaButton = selectMediaButton;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static FragmentSelectMediaBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static FragmentSelectMediaBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.fragment_select_media, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static FragmentSelectMediaBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.progressLayout;
      ProgressBar progressLayout = ViewBindings.findChildViewById(rootView, id);
      if (progressLayout == null) {
        break missingId;
      }

      id = R.id.selectMediaButton;
      Button selectMediaButton = ViewBindings.findChildViewById(rootView, id);
      if (selectMediaButton == null) {
        break missingId;
      }

      return new FragmentSelectMediaBinding((ConstraintLayout) rootView, progressLayout,
          selectMediaButton);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}