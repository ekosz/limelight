#- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the MIT License.

on_cast do
  radio_button = Java::limelight.ui.model.inputs.RadioButtonPanel.new
  peer.add(radio_button)
  @radio_button = radio_button
end

attr_accessor :radio_button #:nodoc:
attr_reader :button_group #:nodoc:

# Sets the radio button group to which this radio button belongs.
#
def group=(group_name)
  @button_group = scene.button_groups.get(group_name)
  @button_group.add(@radio_button)
end

# Checks or unchecks this radio button.
#
def checked=(value)
  @radio_button.selected = value
end

alias :selected= :checked=

# Returns true is this radio is button is checked.
#
def checked
  return @radio_button.is_selected
end

alias :checked? :checked
alias :selected? :checked
alias :selected :checked
